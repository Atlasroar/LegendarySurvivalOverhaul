package sfiomn.legendarysurvivaloverhaul.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Dependency-free executable regression tests; run with the accompanying Gradle init script. */
public final class ConfigFallbackTest {
    public enum Choice { FIRST, SECOND }

    public static final class Settings extends ConfigData {
        public ConfigValue.ValidatedBoolean enabled = new ConfigValue.ValidatedBoolean(true);
        public ConfigValue.ValidatedInt count = new ConfigValue.ValidatedInt(4, 20, 1);
        public ConfigValue.ValidatedDouble radius = new ConfigValue.ValidatedDouble(6.0, 32.0, 1.0);
        public ConfigValue.ValidatedEnum<Choice> choice = new ConfigValue.ValidatedEnum<>(Choice.FIRST);
        public ConfigValue.ValidatedList<String> slots =
                new ConfigValue.ValidatedString("legs/belt", "[a-z0-9_.-]+/[a-z0-9_.-]+").toList(List.of("legs/belt"));
        public ConfigValue.ValidatedList<Double> thresholds =
                new ConfigValue.ValidatedDouble(0, 1, 0).toList(List.of(0.2, 0.5));

        public Settings() { super("test"); }
    }

    public static void main(String[] args) throws Exception {
        net.minecraft.SharedConstants.tryDetectVersion();
        net.minecraft.server.Bootstrap.bootStrap();
        Path directory = Path.of("build", "config-regression");
        Files.createDirectories(directory);
        LegendarySurvivalOverhaul.modConfigPath = directory;
        try {
            persistence(directory);
            migration(directory);
            synchronization();
            absenceSafety();
            System.out.println("PASS: config TOML, validation, synchronization, restoration and optional-class isolation");
        } finally {
            try (var files = Files.list(directory)) {
                for (Path file : files.toList()) Files.delete(file);
            }
            Files.delete(directory);
        }
    }

    private static void migration(Path directory) throws Exception {
        Files.writeString(directory.resolve("common.toml"), """
                [core.advanced]
                "Routine Packet Sync" = 47
                [food]
                "Base Food Exhaustion" = 0.3
                """);
        var old = ForgeConfigMigration.readLegacyFile(directory, "common");
        check(old != null && Files.exists(directory.resolve("common.toml.forge-backup")), "Forge backup");
        CommonConfig common = new CommonConfig();
        ForgeConfigMigration.apply("common", common, old);
        check(common.routinePacketSync.get() == 47 && common.baseFoodExhaustion.get() == 0.3, "Forge migration without Fzzy");
        Files.writeString(directory.resolve("common.toml"), """
                routinePacketSync = 47
                airQualityEnabled = false
                drownedChoking = 86
                yellowAirProviderRadius = 11.0
                """);
        var airValues = AirConfigMigration.readCommon(directory);
        AirConfig air = new AirConfig();
        AirConfigMigration.apply(air, airValues);
        AirConfigMigration.removeMigratedFields(directory);
        check(!air.airQualityEnabled.get() && air.drownedChoking.get() == 86
                && air.yellowAirProviderRadius.get() == 11, "air migration without Fzzy");
        String updated = Files.readString(directory.resolve("common.toml"));
        check(updated.contains("routinePacketSync") && !updated.contains("drownedChoking"), "air migration retains unrelated fields");
        check(Files.exists(directory.resolve("common.toml.air-backup")), "air backup");
    }

    private static void persistence(Path directory) throws Exception {
        Path file = directory.resolve("test.toml");
        Files.writeString(file, """
                # Existing Fzzy-format flat TOML, including multiline lists and inline comments.
                enabled = false
                count = 999 # corrected by the same range used by the editor
                radius = 0.5
                choice = "SECOND"
                slots = [
                  "legs/belt",
                  "hand/ring",
                ]
                thresholds = [0.2, 0.7]
                unknownFutureSetting = "retained"
                """);
        Settings settings = new Settings();
        FileConfigStore.load(settings);
        check(!settings.enabled.get(), "boolean TOML value");
        check(settings.count.get() == 20 && settings.radius.get() == 1, "numeric bounds");
        check(settings.choice.get() == Choice.SECOND, "enum TOML value");
        check(settings.slots.get().equals(List.of("legs/belt", "hand/ring")), "multiline list");
        check(settings.thresholds.get().equals(List.of(0.2, 0.7)), "numeric list");
        settings.save();
        check(Files.readString(file).contains("unknownFutureSetting"), "unknown values preserved");
        Settings reread = new Settings();
        FileConfigStore.load(reread);
        check(FileConfigStore.snapshot(List.of(settings)).equals(FileConfigStore.snapshot(List.of(reread))), "TOML round trip");
        Files.writeString(file, "count = \"not an integer\"\nslots = [\"invalid\"]\n");
        Settings invalid = new Settings();
        FileConfigStore.load(invalid);
        check(invalid.count.get() == 4 && invalid.slots.get().equals(List.of("legs/belt")), "invalid values retain defaults");
        Files.writeString(file, "not valid toml [");
        FileConfigStore.load(new Settings());
        check(Files.readString(file).equals("not valid toml ["), "malformed file is not overwritten");
    }

    private static void synchronization() {
        Settings client = new Settings();
        ClientConfig clientOnly = new ClientConfig();
        List<ConfigData> configs = List.of(client, clientOnly);
        JsonObject local = FileConfigStore.snapshot(configs);
        check(!local.has("client"), "client-only settings never synchronize");
        Settings server = new Settings();
        server.count.validateAndSet(12);
        server.choice.validateAndSet(Choice.SECOND);
        server.slots.validateAndSet(List.of());
        JsonObject remote = JsonParser.parseString(FileConfigStore.snapshot(List.of(server)).toString()).getAsJsonObject();
        FileConfigStore.apply(configs, remote);
        check(client.count.get() == 12 && client.choice.get() == Choice.SECOND && client.slots.get().isEmpty(), "server values applied");
        JsonObject invalid = remote.deepCopy();
        invalid.getAsJsonObject("test").addProperty("count", 8);
        invalid.getAsJsonObject("test").addProperty("choice", "INVALID");
        try {
            FileConfigStore.apply(configs, invalid);
            throw new AssertionError("Invalid packet accepted");
        } catch (IllegalArgumentException expected) {
            check(client.count.get() == 12, "invalid packet has no partial effects");
        }
        FileConfigStore.apply(configs, local);
        check(client.count.get() == 4 && client.choice.get() == Choice.FIRST
                && client.slots.get().equals(List.of("legs/belt")), "disconnect restores local configuration");
        try {
            client.radius.validateAndSet(Double.NaN);
            throw new AssertionError("NaN accepted");
        } catch (IllegalArgumentException expected) {
            check(client.radius.get() == 6, "nonfinite values rejected");
        }
        try {
            client.thresholds.get().add(0.8);
            throw new AssertionError("Mutable config list");
        } catch (UnsupportedOperationException expected) {}
    }

    private static void absenceSafety() throws Exception {
        for (String optional : List.of("me.fzzyhmstrs.fzzy_config.config.Config", "kotlin.Unit",
                "dev.emi.trinkets.api.TrinketsApi")) {
            try {
                Class.forName(optional, false, ConfigFallbackTest.class.getClassLoader());
                throw new AssertionError("Optional library unexpectedly present in fallback test: " + optional);
            } catch (ClassNotFoundException expected) {}
        }
        for (String name : List.of("Config", "ConfigData", "ConfigValue", "CommonConfig", "AirConfig",
                "ClientConfig", "TemperatureConfig", "SeasonsConfig", "ThirstConfig", "HealthConfig",
                "BodyDamageConfig", "TrinketsConfig", "ForgeConfigMigration", "AirConfigMigration")) {
            Class<?> type = Class.forName("sfiomn.legendarysurvivaloverhaul.config." + name, false,
                    ConfigFallbackTest.class.getClassLoader());
            // Resolves every method/field signature without initializing Minecraft registries.
            type.getDeclaredFields();
            type.getDeclaredMethods();
        }
        // Loading these APIs must work with neither Fzzy nor the Kotlin standard library on the classpath.
        check(new CommonConfig().routinePacketSync.get() == 30, "real schema default");
        check(new TrinketsConfig().thermometerSlots.get().equals(List.of("legs/belt")), "optional Trinkets schema default");
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
