package sfiomn.legendarysurvivaloverhaul.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;

import java.util.Set;

@SuppressWarnings("unused")
public class AirConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
    static final Set<String> LEGACY_FIELDS = Set.of("airQualityEnabled", "enableSignalTorches", "drownedChoking",
            "yellowAirProviderRadius", "blueAirProviderRadius", "redAirProviderRadius", "greenAirProviderRadius");

    public AirConfig() {
        super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "air"));
    }

    @Override
    public void onSyncClient() {
        Config.bake(this);
    }

    @Override
    public void onSyncServer() {
        Config.bake(this);
    }

    @Override
    public void onUpdateClient() {
        Config.bake(this);
    }

    @Override
    public void onUpdateServer(ServerUpdateContext context) {
        Config.bake(this);
    }

    public ConfigGroup group_general = new ConfigGroup("group_general");

    @Comment("Enable air-quality breathing outside liquids. Disabling this restores vanilla air handling.")
    public ValidatedBoolean airQualityEnabled = new ValidatedBoolean(true);
    @Comment("Allow normal torches to be toggled into cosmetic Signal Torches with an empty main hand.")
    public ValidatedBoolean enableSignalTorches = new ValidatedBoolean(true);
    @Comment("Air removed by a Drowned melee attack. Set to 0 to disable.")
    @ConfigGroup.Pop
    public ValidatedInt drownedChoking = integer(100, 72000, 0);

    public ConfigGroup group_providers = new ConfigGroup("group_providers");

    @Comment("YELLOW provider radius in blocks. Large radii substantially increase nearby-block scanning cost.")
    public ValidatedDouble yellowAirProviderRadius = radius(6.0);
    @Comment("BLUE provider radius in blocks (including soul sources). Large radii increase scanning cost.")
    public ValidatedDouble blueAirProviderRadius = radius(6.0);
    @Comment("RED provider radius in blocks (including source and flowing lava). Hazards win over safer providers.")
    public ValidatedDouble redAirProviderRadius = radius(3.0);
    @Comment("GREEN provider radius in blocks (including portals). Large radii increase scanning cost.")
    @ConfigGroup.Pop
    public ValidatedDouble greenAirProviderRadius = radius(9.0);

    public ConfigGroup group_dimensions = new ConfigGroup("group_dimensions");

    @Comment("Override vanilla-dimension ambient air profiles with the settings below. Disabled preserves datapack profiles and the existing Nether YELLOW fallback. Nearby providers and fluids still take precedence.")
    public ValidatedBoolean overrideVanillaDimensionProfiles = new ValidatedBoolean(false);
    @Comment("Lowest eye-block Y inside the Overworld range, inclusive. Used only when dimension overrides are enabled.")
    public ValidatedInt overworldMinY = integer(0, 2048, -2048);
    @Comment("Highest eye-block Y inside the Overworld range, inclusive. Must be at least the minimum Y. Used only when dimension overrides are enabled.")
    public ValidatedInt overworldMaxY = integer(255, 2048, -2048);
    @Comment("Overworld ambient air inside the configured inclusive eye-height range, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> overworldAir = new ValidatedEnum<>(AirQualityLevel.GREEN);
    @Comment("Overworld ambient air outside the configured eye-height range, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> overworldOutsideAir = new ValidatedEnum<>(AirQualityLevel.YELLOW);
    @Comment("Nether ambient air at all heights, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> netherAir = new ValidatedEnum<>(AirQualityLevel.YELLOW);
    @Comment("End ambient air at all heights, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> endAir = new ValidatedEnum<>(AirQualityLevel.RED);
    @Comment("Ambient quality for dimensions without a datapack profile. Does not replace configured custom dimensions.")
    @ConfigGroup.Pop
    public ValidatedEnum<AirQualityLevel> unconfiguredDimensionAir = new ValidatedEnum<>(AirQualityLevel.GREEN);

    public ConfigGroup group_breathing = new ConfigGroup("group_breathing");

    @Comment("Ticks between YELLOW air drain attempts outside the Nether. 20 ticks = one second.")
    public ValidatedInt yellowDrainInterval = integer(4, 72000, 1);
    @Comment("Ticks between YELLOW air drain attempts in the Nether.")
    public ValidatedInt netherYellowDrainInterval = integer(2, 72000, 1);
    @Comment("Ticks between RED air drain attempts, including submerged breathing. Default 1 preserves underwater-like drain.")
    public ValidatedInt redDrainInterval = integer(1, 72000, 1);
    @Comment("Air removed per successful drain attempt in YELLOW/RED air. Respiration and breathing protection still apply.")
    public ValidatedInt airDrainAmount = integer(1, 300, 1);
    @Comment("Air restored per tick in GREEN air.")
    public ValidatedInt greenAirRefillAmount = integer(4, 300, 1);
    @Comment("Ticks between one-point durability damage to protective breathing equipment. 300 = 15 seconds; 0 disables wear.")
    public ValidatedInt breathingEquipmentDamageInterval = integer(300, 72000, 0);
    @Comment("Damage from air-quality suffocation outside water. Vanilla underwater drowning damage is unchanged. Set 0 to disable this damage.")
    @ConfigGroup.Pop
    public ValidatedDouble suffocationDamage = new ValidatedDouble(2.0, 100.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

    public ConfigGroup group_bladder = new ConfigGroup("group_bladder");

    @Comment("Air Bladder durability restored per use tick in GREEN air, for both bladder variants.")
    public ValidatedInt airBladderRechargeAmount = integer(4, 300, 1);
    @Comment("Maximum air restored per Air Bladder use tick in non-refilling air. Each point costs one durability.")
    public ValidatedInt airBladderRefillAmount = integer(4, 300, 1);
    @Comment("Air Bladder cooldown after filling the player's air in non-refilling air. 150 = 7.5 seconds; 0 disables cooldown.")
    @ConfigGroup.Pop
    public ValidatedInt airBladderCooldown = integer(150, 72000, 0);

    private static ValidatedInt integer(int value, int max, int min) {
        return new ValidatedInt(value, max, min, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);
    }

    private static ValidatedDouble radius(double value) {
        return new ValidatedDouble(value, 32.0, 1.0, ValidatedNumber.WidgetType.TEXTBOX);
    }
}
