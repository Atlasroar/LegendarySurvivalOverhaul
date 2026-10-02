package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class AirConfigMigration {
    private static final Pattern ENTRY = Pattern.compile("^\\s*([A-Za-z]+)\\s*=\\s*([^#]+?)(?:\\s*#.*)?$");

    private AirConfigMigration() {}

    static Map<String, String> readCommon(Path directory) {
        Path common = directory.resolve("common.toml");
        Map<String, String> values = new HashMap<>();
        if (!Files.isRegularFile(common)) return values;
        try {
            for (String line : Files.readAllLines(common, StandardCharsets.UTF_8)) {
                Matcher matcher = ENTRY.matcher(line);
                if (matcher.matches() && AirConfig.LEGACY_FIELDS.contains(matcher.group(1))) {
                    values.put(matcher.group(1), matcher.group(2).trim());
                }
            }
            if (!values.isEmpty()) {
                Path backup = directory.resolve("common.toml.air-backup");
                if (!Files.exists(backup)) Files.copy(common, backup);
                LegendarySurvivalOverhaul.LOGGER.info("Backed up Common config before moving air settings to air.toml");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read Common air settings for migration: " + common, e);
        }
        return values;
    }

    static void apply(AirConfig air, Map<String, String> values) {
        if (values.isEmpty()) return;
        air.airQualityEnabled.validateAndSet(bool(values, "airQualityEnabled", air.airQualityEnabled.get()));
        air.enableSignalTorches.validateAndSet(bool(values, "enableSignalTorches", air.enableSignalTorches.get()));
        air.drownedChoking.validateAndSet((int) number(values, "drownedChoking", air.drownedChoking.get()));
        air.yellowAirProviderRadius.validateAndSet(number(values, "yellowAirProviderRadius", air.yellowAirProviderRadius.get()));
        air.blueAirProviderRadius.validateAndSet(number(values, "blueAirProviderRadius", air.blueAirProviderRadius.get()));
        air.redAirProviderRadius.validateAndSet(number(values, "redAirProviderRadius", air.redAirProviderRadius.get()));
        air.greenAirProviderRadius.validateAndSet(number(values, "greenAirProviderRadius", air.greenAirProviderRadius.get()));
        air.save();
        LegendarySurvivalOverhaul.LOGGER.info("Migrated {} Common air settings into air.toml", values.size());
    }

    static void removeMigratedFields(Path directory) {
        Path common = directory.resolve("common.toml");
        try {
            StringBuilder updated = new StringBuilder();
            for (String line : Files.readAllLines(common, StandardCharsets.UTF_8)) {
                Matcher matcher = ENTRY.matcher(line);
                if (!matcher.matches() || !AirConfig.LEGACY_FIELDS.contains(matcher.group(1)))
                    updated.append(line).append('\n');
            }
            Files.writeString(common, updated, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot remove migrated air settings from " + common, e);
        }
    }

    private static boolean bool(Map<String, String> values, String key, boolean fallback) {
        String raw = values.get(key);
        if (raw == null) return fallback;
        if ("true".equals(raw)) return true;
        if ("false".equals(raw)) return false;
        throw new IllegalArgumentException("Invalid Common air setting " + key + " = " + raw);
    }

    private static double number(Map<String, String> values, String key, double fallback) {
        String raw = values.get(key);
        if (raw == null) return fallback;
        double value = Double.parseDouble(raw);
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Non-finite Common air setting " + key);
        return value;
    }
}
