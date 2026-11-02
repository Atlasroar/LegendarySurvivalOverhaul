package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import sfiomn.legendarysurvivaloverhaul.config.*;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;

import java.util.Set;

@SuppressWarnings("unused")
public class FzzyAirConfig extends me.fzzyhmstrs.fzzy_config.config.Config {

    public FzzyAirConfig() {
        super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "air"));
    }

    @Override
    public void onSyncClient() {
        FzzyConfigBridge.bake(this, "air");
    }

    @Override
    public void onSyncServer() {
        FzzyConfigBridge.bake(this, "air");
    }

    @Override
    public void onUpdateClient() {
        FzzyConfigBridge.bake(this, "air");
    }

    @Override
    public void onUpdateServer(ServerUpdateContext context) {
        FzzyConfigBridge.bake(this, "air");
    }

    public ConfigGroup group_general = new ConfigGroup("group_general");

    @Comment("Enable air-quality breathing outside liquids. Disabling this restores vanilla air handling.")
    public ValidatedBoolean airQualityEnabled = FzzyConfigBridge.field("air", "airQualityEnabled");
    @Comment("Allow normal torches to be toggled into cosmetic Signal Torches with an empty main hand.")
    public ValidatedBoolean enableSignalTorches = FzzyConfigBridge.field("air", "enableSignalTorches");
    @Comment("Air removed by a Drowned melee attack. Set to 0 to disable.")
    @ConfigGroup.Pop
    public ValidatedInt drownedChoking = FzzyConfigBridge.field("air", "drownedChoking");

    public ConfigGroup group_providers = new ConfigGroup("group_providers");

    @Comment("YELLOW provider radius in blocks. Large radii substantially increase nearby-block scanning cost.")
    public ValidatedDouble yellowAirProviderRadius = FzzyConfigBridge.field("air", "yellowAirProviderRadius");
    @Comment("BLUE provider radius in blocks (including soul sources). Large radii increase scanning cost.")
    public ValidatedDouble blueAirProviderRadius = FzzyConfigBridge.field("air", "blueAirProviderRadius");
    @Comment("RED provider radius in blocks (including source and flowing lava). Hazards win over safer providers.")
    public ValidatedDouble redAirProviderRadius = FzzyConfigBridge.field("air", "redAirProviderRadius");
    @Comment("GREEN provider radius in blocks (including portals). Large radii increase scanning cost.")
    @ConfigGroup.Pop
    public ValidatedDouble greenAirProviderRadius = FzzyConfigBridge.field("air", "greenAirProviderRadius");

    public ConfigGroup group_dimensions = new ConfigGroup("group_dimensions");

    @Comment("Override vanilla-dimension ambient air profiles with the settings below. Disabled preserves datapack profiles and the existing Nether YELLOW fallback. Nearby providers and fluids still take precedence.")
    public ValidatedBoolean overrideVanillaDimensionProfiles = FzzyConfigBridge.field("air", "overrideVanillaDimensionProfiles");
    @Comment("Lowest eye-block Y inside the Overworld range, inclusive. Used only when dimension overrides are enabled.")
    public ValidatedInt overworldMinY = FzzyConfigBridge.field("air", "overworldMinY");
    @Comment("Highest eye-block Y inside the Overworld range, inclusive. Must be at least the minimum Y. Used only when dimension overrides are enabled.")
    public ValidatedInt overworldMaxY = FzzyConfigBridge.field("air", "overworldMaxY");
    @Comment("Overworld ambient air inside the configured inclusive eye-height range, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> overworldAir = FzzyConfigBridge.field("air", "overworldAir");
    @Comment("Overworld ambient air outside the configured eye-height range, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> overworldOutsideAir = FzzyConfigBridge.field("air", "overworldOutsideAir");
    @Comment("Nether ambient air at all heights, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> netherAir = FzzyConfigBridge.field("air", "netherAir");
    @Comment("End ambient air at all heights, when overrides are enabled.")
    public ValidatedEnum<AirQualityLevel> endAir = FzzyConfigBridge.field("air", "endAir");
    @Comment("Ambient quality for dimensions without a datapack profile. Does not replace configured custom dimensions.")
    @ConfigGroup.Pop
    public ValidatedEnum<AirQualityLevel> unconfiguredDimensionAir = FzzyConfigBridge.field("air", "unconfiguredDimensionAir");

    public ConfigGroup group_breathing = new ConfigGroup("group_breathing");

    @Comment("Ticks between YELLOW air drain attempts outside the Nether. 20 ticks = one second.")
    public ValidatedInt yellowDrainInterval = FzzyConfigBridge.field("air", "yellowDrainInterval");
    @Comment("Ticks between YELLOW air drain attempts in the Nether.")
    public ValidatedInt netherYellowDrainInterval = FzzyConfigBridge.field("air", "netherYellowDrainInterval");
    @Comment("Ticks between RED air drain attempts, including submerged breathing. Default 1 preserves underwater-like drain.")
    public ValidatedInt redDrainInterval = FzzyConfigBridge.field("air", "redDrainInterval");
    @Comment("Air removed per successful drain attempt in YELLOW/RED air. Respiration and breathing protection still apply.")
    public ValidatedInt airDrainAmount = FzzyConfigBridge.field("air", "airDrainAmount");
    @Comment("Air restored per tick in GREEN air.")
    public ValidatedInt greenAirRefillAmount = FzzyConfigBridge.field("air", "greenAirRefillAmount");
    @Comment("Ticks between one-point durability damage to protective breathing equipment. 300 = 15 seconds; 0 disables wear.")
    public ValidatedInt breathingEquipmentDamageInterval = FzzyConfigBridge.field("air", "breathingEquipmentDamageInterval");
    @Comment("Damage from air-quality suffocation outside water. Vanilla underwater drowning damage is unchanged. Set 0 to disable this damage.")
    @ConfigGroup.Pop
    public ValidatedDouble suffocationDamage = FzzyConfigBridge.field("air", "suffocationDamage");

    public ConfigGroup group_bladder = new ConfigGroup("group_bladder");

    @Comment("Air Bladder durability restored per use tick in GREEN air, for both bladder variants.")
    public ValidatedInt airBladderRechargeAmount = FzzyConfigBridge.field("air", "airBladderRechargeAmount");
    @Comment("Maximum air restored per Air Bladder use tick in non-refilling air. Each point costs one durability.")
    public ValidatedInt airBladderRefillAmount = FzzyConfigBridge.field("air", "airBladderRefillAmount");
    @Comment("Air Bladder cooldown after filling the player's air in non-refilling air. 150 = 7.5 seconds; 0 disables cooldown.")
    @ConfigGroup.Pop
    public ValidatedInt airBladderCooldown = FzzyConfigBridge.field("air", "airBladderCooldown");

    public ConfigGroup group_compatibility = new ConfigGroup("group_compatibility");

    @Comment("When VanillaBackport is installed, sulfur caves have YELLOW ambient air with the Nether drain interval. Fluids and nearby providers take precedence. Disable to use the normal dimension profile.")
    public ValidatedBoolean sulfurCaveAirEnabled = FzzyConfigBridge.field("air", "sulfurCaveAirEnabled");
    // Retain the beta.1 field key so existing saved preferences migrate without resetting.
    @Comment("An equipped Respirator prevents new Nausea effects from any source, even without VanillaBackport. Held masks do not protect. Does not clear existing effects. Independent of Enable Air Quality.")
    @ConfigGroup.Pop
    public ValidatedBoolean respiratorBlocksSulfurNausea = FzzyConfigBridge.field("air", "respiratorBlocksSulfurNausea");

}
