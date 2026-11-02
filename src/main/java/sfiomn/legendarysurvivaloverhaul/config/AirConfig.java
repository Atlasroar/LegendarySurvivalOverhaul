package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedEnum;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;

import java.util.Set;

@SuppressWarnings("unused")
public class AirConfig extends ConfigData {
    static final Set<String> LEGACY_FIELDS = Set.of("airQualityEnabled", "enableSignalTorches", "drownedChoking",
            "yellowAirProviderRadius", "blueAirProviderRadius", "redAirProviderRadius", "greenAirProviderRadius");

    public AirConfig() {
        super("air");
    }

    public ValidatedBoolean airQualityEnabled = new ValidatedBoolean(true);

    public ValidatedBoolean enableSignalTorches = new ValidatedBoolean(true);

    public ValidatedInt drownedChoking = integer(100, 72000, 0);

    public ValidatedDouble yellowAirProviderRadius = radius(6.0);

    public ValidatedDouble blueAirProviderRadius = radius(6.0);

    public ValidatedDouble redAirProviderRadius = radius(3.0);

    public ValidatedDouble greenAirProviderRadius = radius(9.0);

    public ValidatedBoolean overrideVanillaDimensionProfiles = new ValidatedBoolean(false);

    public ValidatedInt overworldMinY = integer(0, 2048, -2048);

    public ValidatedInt overworldMaxY = integer(255, 2048, -2048);

    public ValidatedEnum<AirQualityLevel> overworldAir = new ValidatedEnum<>(AirQualityLevel.GREEN);

    public ValidatedEnum<AirQualityLevel> overworldOutsideAir = new ValidatedEnum<>(AirQualityLevel.YELLOW);

    public ValidatedEnum<AirQualityLevel> netherAir = new ValidatedEnum<>(AirQualityLevel.YELLOW);

    public ValidatedEnum<AirQualityLevel> endAir = new ValidatedEnum<>(AirQualityLevel.RED);

    public ValidatedEnum<AirQualityLevel> unconfiguredDimensionAir = new ValidatedEnum<>(AirQualityLevel.GREEN);

    public ValidatedInt yellowDrainInterval = integer(4, 72000, 1);

    public ValidatedInt netherYellowDrainInterval = integer(2, 72000, 1);

    public ValidatedInt redDrainInterval = integer(1, 72000, 1);

    public ValidatedInt airDrainAmount = integer(1, 300, 1);

    public ValidatedInt greenAirRefillAmount = integer(4, 300, 1);

    public ValidatedInt breathingEquipmentDamageInterval = integer(300, 72000, 0);

    public ValidatedDouble suffocationDamage = new ValidatedDouble(2.0, 100.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

    public ValidatedInt airBladderRechargeAmount = integer(4, 300, 1);

    public ValidatedInt airBladderRefillAmount = integer(4, 300, 1);

    public ValidatedInt airBladderCooldown = integer(150, 72000, 0);

    public ValidatedBoolean sulfurCaveAirEnabled = new ValidatedBoolean(true);
    // Retain the beta.1 field key so existing saved preferences migrate without resetting.

    public ValidatedBoolean respiratorBlocksSulfurNausea = new ValidatedBoolean(true);

    private static ValidatedInt integer(int value, int max, int min) {
        return new ValidatedInt(value, max, min, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);
    }

    private static ValidatedDouble radius(double value) {
        return new ValidatedDouble(value, 32.0, 1.0, ValidatedNumber.WidgetType.TEXTBOX);
    }
}
