package sfiomn.legendarysurvivaloverhaul.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import java.util.Collections;
import java.util.List;

/**
 * Temperature settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class TemperatureConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public TemperatureConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature"));
	}

	@Override
	public void onSyncClient()
	{
		Config.bake(this);
	}

	@Override
	public void onSyncServer()
	{
		Config.bake(this);
	}

	@Override
	public void onUpdateClient()
	{
		Config.bake(this);
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		Config.bake(this);
	}

	@Comment("Whether the temperature system is enabled. Season related temperature options are in seasons.toml.")
	public ValidatedBoolean temperatureEnabled = new ValidatedBoolean(true);

	@Comment("If enabled, players will take damage from the effects of high temperature.")
	public ValidatedBoolean dangerousHeatTemperature = new ValidatedBoolean(true);

	@Comment("If enabled, players will take damage from the effects of low temperature.")
	public ValidatedBoolean dangerousColdTemperature = new ValidatedBoolean(true);

	@Comment("Chance of the ferns to become a gold fern when grow mature.")
	public ValidatedDouble goldFernChance = new ValidatedDouble(0.01, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	@Comment("How much of an effect being on fire has on a player's temperature.")
	public ValidatedDouble onFireModifier = new ValidatedDouble(12.5, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much of an effect sprinting has on a player's temperature.")
	public ValidatedDouble sprintModifier = new ValidatedDouble(1.5, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much of an effect altitude has on player's temperature. Each 64 blocks further from sea level will impact player's temperature by this value. The sea level can be defined via datapack under the dimension's temperature. As an example, a value of -6 will reduce the player's temperature by 6 for each 64 blocks (the calculus is done linearly).")
	public ValidatedDouble altitudeModifier = new ValidatedDouble(-6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_temperature_immunity = new ConfigGroup("group_temperature_immunity");

	@Comment("If enabled, players will be immune to temperature effects after death.")
	public ValidatedBoolean temperatureImmunityOnDeathEnabled = new ValidatedBoolean(true);

	@Comment("Temperature immunity period in ticks while the player is immune to temperature effects after death.")
	public ValidatedInt temperatureImmunityOnDeathTime = new ValidatedInt(1800, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("If enabled, players will be immune to temperature effects on first spawn in a world.")
	public ValidatedBoolean temperatureImmunityOnFirstSpawnEnabled = new ValidatedBoolean(true);

	@Comment("Temperature immunity period in ticks while the player is immune to temperature effects on first spawn.")
	@ConfigGroup.Pop
	public ValidatedInt temperatureImmunityOnFirstSpawnTime = new ValidatedInt(1800, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_secondary_effects = new ConfigGroup("group_secondary_effects");

	@Comment("If enabled, players will also receive other effects from their current temperature state. If the player is too hot, hydration will deplete faster.")
	public ValidatedBoolean heatTemperatureSecondaryEffects = new ValidatedBoolean(true);

	@Comment("If enabled, players will also receive other effects from their current temperature state. If the player is too cold, hunger will deplete faster.")
	public ValidatedBoolean coldTemperatureSecondaryEffects = new ValidatedBoolean(true);

	@Comment("How much thirst exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from heat.")
	public ValidatedDouble heatThirstEffectModifier = new ValidatedDouble(0.2d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much food exhaustion will be added every 50 ticks with no amplification effect, when the player suffers from frostbite. As reference, the hunger effect add 0.025 food exhaustion every 50 ticks.")
	@ConfigGroup.Pop
	public ValidatedDouble coldHungerEffectModifier = new ValidatedDouble(0.1d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_wetness = new ConfigGroup("group_wetness");

	@Comment("Enable the wetness mechanic.")
	public ValidatedBoolean wetnessEnabled = new ValidatedBoolean(true);

	@Comment("List of mounts that provide a wetness immunity.")
	public ValidatedList<String> wetnessImmunityMounts = ValidatedList.ofString(List.of("alexscaves:submarine", "immersive_machinery:bamboo_bee", "immersive_machinery:tunnel_digger", "immersive_machinery:redstone_sheep", "immersive_machinery:copperfin"));

	@Comment("How much being wet influences the player's temperature. It means that for a value of -10, the body temperature of the player is reduced by 10.")
	public ValidatedDouble wetMultiplier = new ValidatedDouble(-10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How frequently the wetness is modified. By default, every 10 ticks, the wetness will either increase or decrease, based on the conditions.")
	public ValidatedInt wetnessTickTimer = new ValidatedInt(10, 100000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("How much the wetness decrease when out of water.")
	public ValidatedInt wetnessDecrease = new ValidatedInt(-3, 0, -1000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("How much the wetness increase when under rain.")
	public ValidatedInt wetnessRainIncrease = new ValidatedInt(7, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("How much the wetness increase when the player is in a fluid, scale by the amount of fluid in the block. The defined value is for a full block of fluid, and goes up to 2 times this value when fully immerge.")
	@ConfigGroup.Pop
	public ValidatedInt wetnessFluidIncrease = new ValidatedInt(10, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_huddling = new ConfigGroup("group_huddling");

	@Comment("How much nearby players increase the ambient temperature by. Note that this value stacks!")
	public ValidatedDouble playerHuddlingModifier = new ValidatedDouble(0.5, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("The radius, in blocks, around which players will add to each other's temperature.")
	@ConfigGroup.Pop
	public ValidatedInt playerHuddlingRadius = new ValidatedInt(1, 10, 0, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_biomes = new ConfigGroup("group_biomes");

	@Comment("How much a biome's temperature effects are multiplied.")
	public ValidatedDouble biomeTemperatureMultiplier = new ValidatedDouble(18.0d, 1000.0, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Whether biomes will have an effect on a player's temperature.")
	public ValidatedBoolean biomeEffectsEnabled = new ValidatedBoolean(true);

	@Comment("How much hot biome's dryness will make nights really cold. Affects only dry (minecraft down fall <0.2) and hot biome. 1 means no dryness effect; 0.5 means the biome temp will be divided by 2 at the middle of the night.")
	@ConfigGroup.Pop
	public ValidatedDouble biomeDrynessMultiplier = new ValidatedDouble(0.2d, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_underground = new ConfigGroup("group_underground");

	@Comment("How much a biomes temperature effects are multiplied when player is underground")
	public ValidatedDouble undergroundBiomeTemperatureMultiplier = new ValidatedDouble(0.8d, 1000.0, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Distance to the World Surface where underground effect will start to be applied. Smaller distance, no underground effect are applied.")
	public ValidatedInt undergroundEffectStartDistanceToWS = new ValidatedInt(10, 400, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Distance to the World Surface where underground effect will be maximal. Bigger distance, the underground effect is maximal. Between the Start and End Distance, the increase of underground effect is linear.")
	@ConfigGroup.Pop
	public ValidatedInt undergroundEffectEndDistanceToWS = new ValidatedInt(16, 400, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_weather = new ConfigGroup("group_weather");

	@Comment("How much of an effect rain has on temperature. It means that for a value of -2, the body temperature of the player is reduced by 2.")
	public ValidatedDouble rainTemperatureModifier = new ValidatedDouble(-2.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much of an effect snow has on temperature. It means that for a value of -6, the body temperature of the player is reduced by 6.")
	@ConfigGroup.Pop
	public ValidatedDouble snowTemperatureModifier = new ValidatedDouble(-6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_freeze = new ConfigGroup("group_freeze");

	@Comment("If enabled, the player suffers vanilla freeze when inside powder snow.")
	public ValidatedBoolean vanillaFreezeEnabled = new ValidatedBoolean(false);

	@Comment("How much of an effect freeze has on temperature when reaching maximum tick time. Starts at 0 and increases linearly.")
	public ValidatedDouble maxFreezeTemperatureModifier = new ValidatedDouble(-10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How long in tick before freeze modifier reaches its maximum effect.")
	@ConfigGroup.Pop
	public ValidatedInt maxFreezeEffectTick = new ValidatedInt(400, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_time = new ConfigGroup("group_time");

	@Comment("How much Time has effect on Temperature. Maximum effect at noon (positive) and midnight (negative), following a sinusoidal")
	public ValidatedDouble timeModifier = new ValidatedDouble(2.0d, Double.POSITIVE_INFINITY, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How strongly time in extreme temperature biomes affect player's temperature. Extreme temperature biomes (like snowy taiga, deserts, ...) will multiply the time based temperature by this value, while temperate biome won't be affected by this value, following a linear.")
	public ValidatedDouble biomeTimeMultiplier = new ValidatedDouble(1.75d, Double.POSITIVE_INFINITY, 1.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Staying in the shade or during cloudy weather will reduce player's temperature by this amount based on time of the day (maximum effect at noon, following sinusoidal). It means that for a value of -6, the body temperature of the player is reduced by 6. Only effective when reaching the threshold and during day time!")
	public ValidatedDouble shadeTimeModifier = new ValidatedDouble(-6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Defines when the biome temperature added by the season temperature (if seasons mod loaded) will trigger a shade effect.")
	@ConfigGroup.Pop
	public ValidatedDouble shadeTimeModifierThreshold = new ValidatedDouble(9.0, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_coat_heating = new ConfigGroup("group_coat_heating");

	public ValidatedDouble heatingCoat1Modifier = new ValidatedDouble(2.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble heatingCoat2Modifier = new ValidatedDouble(3.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble heatingCoat3Modifier = new ValidatedDouble(4.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_coat_cooling = new ConfigGroup("group_coat_cooling");

	public ValidatedDouble coolingCoat1Modifier = new ValidatedDouble(2.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble coolingCoat2Modifier = new ValidatedDouble(3.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble coolingCoat3Modifier = new ValidatedDouble(4.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_coat_thermal = new ConfigGroup("group_coat_thermal");

	public ValidatedDouble thermalCoat1Modifier = new ValidatedDouble(2.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble thermalCoat2Modifier = new ValidatedDouble(3.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble thermalCoat3Modifier = new ValidatedDouble(4.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_advanced = new ConfigGroup("group_advanced");

	@Comment("Maximum influence distance, in blocks, where thermal sources will have an effect on temperature.")
	public ValidatedInt tempInfluenceMaximumDist = new ValidatedInt(20, 40, 1, ValidatedNumber.WidgetType.SLIDER);

	@Comment("How strongly influence distance above the player is reduced for thermal sources to have an effect on temperature.")
	public ValidatedDouble tempInfluenceUpDistMultiplier = new ValidatedDouble(0.75, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	@Comment("How strongly influence distance in water is reduced for thermal sources to have an effect on temperature. The under water maximum distance is defined as the maximum distance * this value")
	public ValidatedDouble tempInfluenceInWaterDistMultiplier = new ValidatedDouble(0.25, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	@Comment("How strongly influence distance outside a structure is reduced for thermal sources to have an effect on temperature. The outside maximum distance is defined as the maximum distance * this value")
	@ConfigGroup.Pop
	public ValidatedDouble tempInfluenceOutsideDistMultiplier = new ValidatedDouble(0.5, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_advanced_temperature_modification = new ConfigGroup("group_advanced_temperature_modification");

	@Comment("Amount of time in ticks between 2 player temperature modification. The bigger is this value, the more time it takes between temperature adjustments.")
	public ValidatedInt tempTickTime = new ValidatedInt(20, Integer.MAX_VALUE, 5, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Maximum amount of temperature the player's temperature can be modified at each temperature tick time. Correspond to the amount of temperature given when temperature difference is maximum, meaning 40.")
	public ValidatedDouble maxTemperatureModification = new ValidatedDouble(1.0, Integer.MAX_VALUE, 0.1, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Minimum amount of temperature the player's temperature can be modified at each temperature tick time. Correspond to the amount of temperature given when there is no temperature difference")
	@ConfigGroup.Pop
	public ValidatedDouble minTemperatureModification = new ValidatedDouble(0.2, Integer.MAX_VALUE, 0.1, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_integration_terrafirmacraft = new ConfigGroup("group_integration_terrafirmacraft");

	@Comment("How much the heat of the item provided by TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
	public ValidatedDouble tfcItemHeatMultiplier = new ValidatedDouble(0.01d, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much the temperature given from TerraFirmaCraft is multiplied. 0 deactivates the impact on temperature.")
	@ConfigGroup.Pop
	public ValidatedDouble tfcTemperatureMultiplier = new ValidatedDouble(1.0d, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);
}
