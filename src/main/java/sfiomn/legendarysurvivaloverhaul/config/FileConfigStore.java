package sfiomn.legendarysurvivaloverhaul.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlParser;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** TOML persistence and the wire representation shared by both configuration backends. */
public final class FileConfigStore {
    private static final Gson GSON = new Gson();

    private FileConfigStore() {}

    static void load(ConfigData config) {
        Path path = path(config);
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                CommentedConfig values = new TomlParser().parse(reader);
                for (Map.Entry<String, ConfigValue<?>> field : config.fields().entrySet()) {
                    if (!values.contains(field.getKey())) continue;
                    try {
                        set(field.getValue(), values.get(field.getKey()));
                    } catch (RuntimeException e) {
                        LegendarySurvivalOverhaul.LOGGER.warn("Invalid config setting {}.{}; retaining default",
                                config.name, field.getKey());
                    }
                }
            } catch (IOException | RuntimeException e) {
                // Never overwrite a malformed user file with defaults.
                LegendarySurvivalOverhaul.LOGGER.error("Cannot read config " + path + "; retaining defaults and leaving the file untouched", e);
            }
        } else {
            save(config);
        }
    }

    static void save(ConfigData config) {
        Path path = path(config);
        Path staged = path.resolveSibling(path.getFileName() + ".saving");
        try {
            Files.createDirectories(path.getParent());
            CommentedConfig values = CommentedConfig.inMemory();
            if (Files.exists(path)) {
                try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    values = new TomlParser().parse(reader);
                }
            }
            for (Map.Entry<String, ConfigValue<?>> field : config.fields().entrySet())
                values.set(field.getKey(), serializable(field.getValue().get()));
            try (Writer writer = Files.newBufferedWriter(staged, StandardCharsets.UTF_8)) {
                new TomlWriter().write(values, writer);
            }
            Files.move(staged, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Cannot save config " + path, e);
        }
    }

    private static Path path(ConfigData config) {
        return LegendarySurvivalOverhaul.modConfigPath.resolve(config.name + ".toml");
    }

    private static Object serializable(Object value) {
        return value instanceof Enum<?> entry ? entry.name() : value;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void set(ConfigValue setting, Object value) {
        setting.validateAndSet(convert(setting, value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object convert(ConfigValue<?> setting, Object value) {
        if (setting instanceof ConfigValue.ValidatedList<?> list) {
            if (!(value instanceof List<?> values)) throw new IllegalArgumentException("Expected list");
            List<Object> converted = new ArrayList<>();
            for (Object element : values) converted.add(convert(list.element, element));
            return converted;
        }
        Object sample = setting.defaultValue;
        if (sample instanceof Enum<?> entry) {
            if (entry.getDeclaringClass().isInstance(value)) return value;
            if (!(value instanceof String text)) throw new IllegalArgumentException("Expected enum name");
            return Enum.valueOf((Class) entry.getDeclaringClass(), text);
        }
        if (sample instanceof Integer && value instanceof Number number) {
            double numeric = number.doubleValue();
            if (!Double.isFinite(numeric) || numeric != Math.rint(numeric))
                throw new IllegalArgumentException("Expected integer");
            return (int) Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, numeric));
        }
        if (sample instanceof Double && value instanceof Number number) return number.doubleValue();
        if (!sample.getClass().isInstance(value)) throw new IllegalArgumentException("Wrong setting type");
        return value;
    }

    public static JsonObject snapshot(Iterable<ConfigData> configs) {
        JsonObject root = new JsonObject();
        for (ConfigData config : configs) {
            if (config instanceof ClientConfig) continue;
            JsonObject fields = new JsonObject();
            config.fields().forEach((name, value) -> fields.add(name, GSON.toJsonTree(value.get())));
            root.add(config.name, fields);
        }
        return root;
    }

    /** Validate the entire packet before changing any live settings. Unknown keys are forward compatible. */
    public static void apply(Iterable<ConfigData> configs, JsonObject root) {
        Map<ConfigValue<?>, Object> pending = new LinkedHashMap<>();
        for (ConfigData config : configs) {
            if (config instanceof ClientConfig || !root.has(config.name)) continue;
            JsonObject values = root.getAsJsonObject(config.name);
            config.fields().forEach((name, setting) -> {
                if (!values.has(name)) return;
                Object value = convert(setting, json(values.get(name)));
                pending.put(setting, validate(setting, value));
            });
        }
        pending.forEach(FileConfigStore::set);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object validate(ConfigValue setting, Object value) {
        return setting.validate(value);
    }

    private static Object json(JsonElement element) {
        if (element.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            element.getAsJsonArray().forEach(value -> list.add(json(value)));
            return list;
        }
        if (!element.isJsonPrimitive()) throw new IllegalArgumentException("Invalid config value");
        var value = element.getAsJsonPrimitive();
        if (value.isBoolean()) return value.getAsBoolean();
        if (value.isNumber()) return value.getAsNumber();
        return value.getAsString();
    }
}
