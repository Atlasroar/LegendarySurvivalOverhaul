package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import sfiomn.legendarysurvivaloverhaul.config.*;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import java.util.List;

/**
 * Temperature settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class FzzyTemperatureConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public FzzyTemperatureConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature"));
	}

	@Override
	public void onSyncClient()
	{
		FzzyConfigBridge.bake(this, "temperature");
	}

	@Override
	public void onSyncServer()
	{
		FzzyConfigBridge.bake(this, "temperature");
	}

	@Override
	public void onUpdateClient()
	{
		FzzyConfigBridge.bake(this, "temperature");
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		FzzyConfigBridge.bake(this, "temperature");
	}

	@Comment("Whether the temperature system is enabled. Season related temperature options are in seasons.toml.")
	public ValidatedBoolean temperatureEnabled = FzzyConfigBridge.field("temperature", "temperatureEnabled");

	@Comment("Optional Immersive Weathering 2.5.0+ storms: exposed snow/sand drives dangerous temperature at twice the normal rate. Hot/Cold Drink blocks the matching storm. A 3x3 sky-blocking roof within 10 blocks above eye level shelters players. Freezing-wind hits cause 10 seconds of Cold Hunger.")
	public ValidatedBoolean immersiveWeatheringStormsEnabled = FzzyConfigBridge.field("temperature", "immersiveWeatheringStormsEnabled");

	@Comment("If enabled, players will take damage from the effects of high temperature.")
	public ValidatedBoolean dangerousHeatTemperature = FzzyConfigBridge.field("temperature", "dangerousHeatTemperature");

	@Comment("If enabled, players will take damage from the effects of low temperature.")
	public ValidatedBoolean dangerousColdTemperature = FzzyConfigBridge.field("temperature", "dangerousColdTemperature");

	@Comment("Chance of the ferns to become a gold fern when grow mature.")
	public ValidatedDouble goldFernChance = FzzyConfigBridge.field("temperature", "goldFernChance");

	@Comment("How much of an effect being on fire has on a player's temperature.")
	public ValidatedDouble onFireModifier = FzzyConfigBridge.field("temperature", "onFireModifier");

	@Comment("How much of an effect sprinting has on a player's temperature.")
	public ValidatedDouble sprintModifier = FzzyConfigBridge.field("temperature", "sprintModifier");

	@Comment("How much of an effect altitude has on player's temperature. Each 64 blocks further from sea level will impact player's temperature by this value. The sea level can be defined via datapack under the dimension's temperature. As an example, a value of -6 will reduce the player's temperature by 6 for each 64 blocks (the calculus is done linearly).")
	public ValidatedDouble altitudeModifier = FzzyConfigBridge.field("temperature", "altitudeModifier");

	public ConfigGroup group_temperature_immunity = new ConfigGroup("group_temperature_immunity");

	@Comment("If enabled, players will be immune to temperature effects after death.")
	public ValidatedBoolean temperatureImmunityOnDeathEnabled = FzzyConfigBridge.field("temperature", "temperatureImmunityOnDeathEnabled");

	@Comment("Temperature immunity period in ticks while the player is immune to temperature effects after death.")
	public ValidatedInt temperatureImmunityOnDeathTime = FzzyConfigBridge.field("temperature", "temperatureImmunityOnDeathTime");

	@Comment("If enabled, players will be immune to temperature effects on first spawn in a world.")
	public ValidatedBoolean temperatureImmunityOnFirstSpawnEnabled = FzzyConfigBridge.field("temperature", "temperatureImmunityOnFirstSpawnEnabled");

	@Comment("Temperature immunity period in ticks while the player is immune to temperature effects on first spawn.")
	@ConfigGroup.Pop
	public ValidatedInt temperatureImmunityOnFirstSpawnTime = FzzyConfigBridge.field("temperature", "temperatureImmunityOnFirstSpawnTime");

	public ConfigGroup group_secondary_effects = new ConfigGroup("group_secondary_effects");

	@Comment("If enabled, players will also receive other effects from their current temperature state. If the player is too hot, hydration will deplete faster.")
	public ValidatedBoolean heatTemperatureSecondaryEffects = FzzyConfigBridge.field("temperature", "heatTemperatureSecondaryEffects");

	@Comment("If enabled, players will also receive other effects from their current temperature state. If the player is too cold, hunger will deplete faster.")
	public ValidatedBoolean coldTemperatureSecondaryEffects = FzzyConfigBridge.field("temperature", "coldTemperatureSecondaryEffects");

	@Comment("How much thirst exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from heat.")
	public ValidatedDouble heatThirstEffectModifier = FzzyConfigBridge.field("temperature", "heatThirstEffectModifier");

	@Comment("How much food exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from frostbite. As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
	@ConfigGroup.Pop
	public ValidatedDouble coldHungerEffectModifier = FzzyConfigBridge.field("temperature", "coldHungerEffectModifier");

	public ConfigGroup group_wetness = new ConfigGroup("group_wetness");

	@Comment("Enable the wetness mechanic.")
	public ValidatedBoolean wetnessEnabled = FzzyConfigBridge.field("temperature", "wetnessEnabled");

	@Comment("List of mounts that provide a wetness immunity.")
	public ValidatedList<String> wetnessImmunityMounts = FzzyConfigBridge.field("temperature", "wetnessImmunityMounts");

	@Comment("How much being wet influences the player's temperature. It means that for a value of -10, the body temperature of the player is reduced by 10.")
	public ValidatedDouble wetMultiplier = FzzyConfigBridge.field("temperature", "wetMultiplier");

	@Comment("How frequently the wetness is modified. By default, every 10 ticks, the wetness will either increase or decrease, based on the conditions.")
	public ValidatedInt wetnessTickTimer = FzzyConfigBridge.field("temperature", "wetnessTickTimer");

	@Comment("How much the wetness decrease when out of water.")
	public ValidatedInt wetnessDecrease = FzzyConfigBridge.field("temperature", "wetnessDecrease");

	@Comment("How much the wetness increase when under rain.")
	public ValidatedInt wetnessRainIncrease = FzzyConfigBridge.field("temperature", "wetnessRainIncrease");

	@Comment("How much the wetness increase when the player is in a fluid, scale by the amount of fluid in the block. The defined value is for a full block of fluid, and goes up to 2 times this value when fully immerge.")
	@ConfigGroup.Pop
	public ValidatedInt wetnessFluidIncrease = FzzyConfigBridge.field("temperature", "wetnessFluidIncrease");

	public ConfigGroup group_huddling = new ConfigGroup("group_huddling");

	@Comment("How much nearby players increase the ambient temperature by. Note that this value stacks!")
	public ValidatedDouble playerHuddlingModifier = FzzyConfigBridge.field("temperature", "playerHuddlingModifier");

	@Comment("The radius, in blocks, around which players will add to each other's temperature.")
	@ConfigGroup.Pop
	public ValidatedInt playerHuddlingRadius = FzzyConfigBridge.field("temperature", "playerHuddlingRadius");

	public ConfigGroup group_biomes = new ConfigGroup("group_biomes");

	@Comment("How much a biome's temperature effects are multiplied.")
	public ValidatedDouble biomeTemperatureMultiplier = FzzyConfigBridge.field("temperature", "biomeTemperatureMultiplier");

	@Comment("Whether biomes will have an effect on a player's temperature.")
	public ValidatedBoolean biomeEffectsEnabled = FzzyConfigBridge.field("temperature", "biomeEffectsEnabled");

	@Comment("How much hot biome's dryness will make nights really cold. Affects only dry (minecraft down fall <0.2) and hot biome. 1 means no dryness effect; 0.5 means the biome temp will be divided by 2 at the middle of the night.")
	@ConfigGroup.Pop
	public ValidatedDouble biomeDrynessMultiplier = FzzyConfigBridge.field("temperature", "biomeDrynessMultiplier");

	public ConfigGroup group_underground = new ConfigGroup("group_underground");

	@Comment("How much a biomes temperature effects are multiplied when player is underground")
	public ValidatedDouble undergroundBiomeTemperatureMultiplier = FzzyConfigBridge.field("temperature", "undergroundBiomeTemperatureMultiplier");

	@Comment("Distance to the World Surface where underground effect will start to be applied. Smaller distance, no underground effect are applied.")
	public ValidatedInt undergroundEffectStartDistanceToWS = FzzyConfigBridge.field("temperature", "undergroundEffectStartDistanceToWS");

	@Comment("Distance to the World Surface where underground effect will be maximal. Bigger distance, the underground effect is maximal. Between the Start and End Distance, the increase of underground effect is linear.")
	@ConfigGroup.Pop
	public ValidatedInt undergroundEffectEndDistanceToWS = FzzyConfigBridge.field("temperature", "undergroundEffectEndDistanceToWS");

	public ConfigGroup group_weather = new ConfigGroup("group_weather");

	@Comment("How much of an effect rain has on temperature. It means that for a value of -2, the body temperature of the player is reduced by 2.")
	public ValidatedDouble rainTemperatureModifier = FzzyConfigBridge.field("temperature", "rainTemperatureModifier");

	@Comment("How much of an effect snow has on temperature. It means that for a value of -6, the body temperature of the player is reduced by 6.")
	@ConfigGroup.Pop
	public ValidatedDouble snowTemperatureModifier = FzzyConfigBridge.field("temperature", "snowTemperatureModifier");

	public ConfigGroup group_freeze = new ConfigGroup("group_freeze");

	@Comment("If enabled, the player suffers vanilla freeze when inside powder snow.")
	public ValidatedBoolean vanillaFreezeEnabled = FzzyConfigBridge.field("temperature", "vanillaFreezeEnabled");

	@Comment("How much of an effect freeze has on temperature when reaching maximum tick time. Starts at 0 and increases linearly.")
	public ValidatedDouble maxFreezeTemperatureModifier = FzzyConfigBridge.field("temperature", "maxFreezeTemperatureModifier");

	@Comment("How long in tick before freeze modifier reaches its maximum effect.")
	@ConfigGroup.Pop
	public ValidatedInt maxFreezeEffectTick = FzzyConfigBridge.field("temperature", "maxFreezeEffectTick");

	public ConfigGroup group_time = new ConfigGroup("group_time");

	@Comment("How much Time has effect on Temperature. Maximum effect at noon (positive) and midnight (negative), following a sinusoidal")
	public ValidatedDouble timeModifier = FzzyConfigBridge.field("temperature", "timeModifier");

	@Comment("How strongly time in extreme temperature biomes affect player's temperature. Extreme temperature biomes (like snowy taiga, deserts, ...) will multiply the time based temperature by this value, while temperate biome won't be affected by this value, following a linear.")
	public ValidatedDouble biomeTimeMultiplier = FzzyConfigBridge.field("temperature", "biomeTimeMultiplier");

	@Comment("Staying in the shade or during cloudy weather will reduce player's temperature by this amount based on time of the day (maximum effect at noon, following sinusoidal). It means that for a value of -6, the body temperature of the player is reduced by 6. Only effective when reaching the threshold and during day time!")
	public ValidatedDouble shadeTimeModifier = FzzyConfigBridge.field("temperature", "shadeTimeModifier");

	@Comment("Defines when the biome temperature added by the season temperature (if seasons mod loaded) will trigger a shade effect.")
	@ConfigGroup.Pop
	public ValidatedDouble shadeTimeModifierThreshold = FzzyConfigBridge.field("temperature", "shadeTimeModifierThreshold");

	public ConfigGroup group_coat_heating = new ConfigGroup("group_coat_heating");

	public ValidatedDouble heatingCoat1Modifier = FzzyConfigBridge.field("temperature", "heatingCoat1Modifier");

	public ValidatedDouble heatingCoat2Modifier = FzzyConfigBridge.field("temperature", "heatingCoat2Modifier");

	@ConfigGroup.Pop
	public ValidatedDouble heatingCoat3Modifier = FzzyConfigBridge.field("temperature", "heatingCoat3Modifier");

	public ConfigGroup group_coat_cooling = new ConfigGroup("group_coat_cooling");

	public ValidatedDouble coolingCoat1Modifier = FzzyConfigBridge.field("temperature", "coolingCoat1Modifier");

	public ValidatedDouble coolingCoat2Modifier = FzzyConfigBridge.field("temperature", "coolingCoat2Modifier");

	@ConfigGroup.Pop
	public ValidatedDouble coolingCoat3Modifier = FzzyConfigBridge.field("temperature", "coolingCoat3Modifier");

	public ConfigGroup group_coat_thermal = new ConfigGroup("group_coat_thermal");

	public ValidatedDouble thermalCoat1Modifier = FzzyConfigBridge.field("temperature", "thermalCoat1Modifier");

	public ValidatedDouble thermalCoat2Modifier = FzzyConfigBridge.field("temperature", "thermalCoat2Modifier");

	@ConfigGroup.Pop
	public ValidatedDouble thermalCoat3Modifier = FzzyConfigBridge.field("temperature", "thermalCoat3Modifier");

	public ConfigGroup group_advanced = new ConfigGroup("group_advanced");

	@Comment("Maximum influence distance, in blocks, where thermal sources will have an effect on temperature.")
	public ValidatedInt tempInfluenceMaximumDist = FzzyConfigBridge.field("temperature", "tempInfluenceMaximumDist");

	@Comment("How strongly influence distance above the player is reduced for thermal sources to have an effect on temperature.")
	public ValidatedDouble tempInfluenceUpDistMultiplier = FzzyConfigBridge.field("temperature", "tempInfluenceUpDistMultiplier");

	@Comment("How strongly influence distance in water is reduced for thermal sources to have an effect on temperature. The under water maximum distance is defined as the maximum distance * this value")
	public ValidatedDouble tempInfluenceInWaterDistMultiplier = FzzyConfigBridge.field("temperature", "tempInfluenceInWaterDistMultiplier");

	@Comment("How strongly influence distance outside a structure is reduced for thermal sources to have an effect on temperature. The outside maximum distance is defined as the maximum distance * this value")
	@ConfigGroup.Pop
	public ValidatedDouble tempInfluenceOutsideDistMultiplier = FzzyConfigBridge.field("temperature", "tempInfluenceOutsideDistMultiplier");

	public ConfigGroup group_advanced_temperature_modification = new ConfigGroup("group_advanced_temperature_modification");

	@Comment("Amount of time in ticks between 2 player temperature modification. The bigger is this value, the more time it takes between temperature adjustments.")
	public ValidatedInt tempTickTime = FzzyConfigBridge.field("temperature", "tempTickTime");

	@Comment("Maximum amount of temperature the player's temperature can be modified at each temperature tick time. Correspond to the amount of temperature given when temperature difference is maximum, meaning 40.")
	public ValidatedDouble maxTemperatureModification = FzzyConfigBridge.field("temperature", "maxTemperatureModification");

	@Comment("Minimum amount of temperature the player's temperature can be modified at each temperature tick time. Correspond to the amount of temperature given when there is no temperature difference")
	@ConfigGroup.Pop
	public ValidatedDouble minTemperatureModification = FzzyConfigBridge.field("temperature", "minTemperatureModification");

	public ConfigGroup group_integration_terrafirmacraft = new ConfigGroup("group_integration_terrafirmacraft");

	@Comment("How much the heat of the item provided by TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
	public ValidatedDouble tfcItemHeatMultiplier = FzzyConfigBridge.field("temperature", "tfcItemHeatMultiplier");

	@Comment("How much the temperature given from TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
	@ConfigGroup.Pop
	public ValidatedDouble tfcTemperatureMultiplier = FzzyConfigBridge.field("temperature", "tfcTemperatureMultiplier");
}
