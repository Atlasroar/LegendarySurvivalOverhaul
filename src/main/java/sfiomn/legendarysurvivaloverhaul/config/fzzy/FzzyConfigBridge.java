package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.ConfigData;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue;
import sfiomn.legendarysurvivaloverhaul.config.FileConfigStore;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/** Only reached when both Fzzy Config and its Kotlin runtime are present. */
public final class FzzyConfigBridge {
    private static final Map<String, me.fzzyhmstrs.fzzy_config.config.Config> CONFIGS = new LinkedHashMap<>();
    private FzzyConfigBridge() {}

    public static void register() {
        register("client", FzzyClientConfig::new, RegisterType.CLIENT);
        register("common", FzzyCommonConfig::new, RegisterType.BOTH);
        register("air", FzzyAirConfig::new, RegisterType.BOTH);
        register("temperature", FzzyTemperatureConfig::new, RegisterType.BOTH);
        register("seasons", FzzySeasonsConfig::new, RegisterType.BOTH);
        register("thirst", FzzyThirstConfig::new, RegisterType.BOTH);
        register("health", FzzyHealthConfig::new, RegisterType.BOTH);
        register("body_damage", FzzyBodyDamageConfig::new, RegisterType.BOTH);
        register("trinkets", FzzyTrinketsConfig::new, RegisterType.BOTH);
    }

    private static <T extends me.fzzyhmstrs.fzzy_config.config.Config> void register(String name, Supplier<T> factory, RegisterType type) {
        T config = ConfigApiJava.registerAndLoadConfig(factory, type);
        CONFIGS.put(name, config);
        bake(config, name);
    }

    /** Keep optional editor fields consistent on mixed-install servers and after disconnect restoration. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void updateFromData() {
        CONFIGS.forEach((name, config) -> data(name).fields().forEach((key, setting) -> {
            try {
                ((ValidatedField) config.getClass().getField(key).get(config)).validateAndSet(setting.get());
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Cannot update Fzzy setting " + key, e);
            }
        }));
    }

    private static ConfigData data(String name) {
        return Config.all().stream().filter(config -> config.name.equals(name)).findFirst().orElseThrow();
    }

    @SuppressWarnings("unchecked")
    static <F extends ValidatedField<?>> F field(String config, String name) {
        return (F) create(data(config).fields().get(name));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ValidatedField<?> create(ConfigValue<?> value) {
        if (value instanceof ConfigValue.ValidatedBoolean bool) return new ValidatedBoolean(bool.defaultValue);
        if (value instanceof ConfigValue.ValidatedEnum enumeration) return new ValidatedEnum((Enum) enumeration.defaultValue);
        if (value instanceof ConfigValue.ValidatedString string)
            return string.regex == null ? new ValidatedString(string.defaultValue)
                    : new ValidatedString(string.defaultValue, string.regex);
        if (value instanceof ConfigValue.ValidatedInt number)
            return new ValidatedInt(number.defaultValue, number.max, number.min,
                    ValidatedNumber.WidgetType.valueOf(number.widget.name()));
        if (value instanceof ConfigValue.ValidatedDouble number)
            return new ValidatedDouble(number.defaultValue, number.max, number.min,
                    ValidatedNumber.WidgetType.valueOf(number.widget.name()));
        if (value instanceof ConfigValue.ValidatedList list)
            return ((ValidatedField) create(list.element)).toList((java.util.List) list.defaultValue);
        throw new IllegalArgumentException("Unknown config field " + value);
    }

    static void bake(me.fzzyhmstrs.fzzy_config.config.Config source, String name) {
        ConfigData target = data(name);
        target.fields().forEach((key, setting) -> {
            try {
                Object value = ((ValidatedField<?>) source.getClass().getField(key).get(source)).get();
                // Enum values are already typed here, unlike TOML/network values.
                if (value instanceof Enum<?> entry) value = entry.name();
                FileConfigStore.set(setting, value);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Cannot read Fzzy setting " + key, e);
            }
        });
        Config.bake(target);
    }
}
