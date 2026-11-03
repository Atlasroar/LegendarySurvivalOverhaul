package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedList;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import java.util.List;

/**
 * Temperature settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class TemperatureConfig extends ConfigData
{
	public TemperatureConfig()
	{
		super("temperature");
	}

	public ValidatedBoolean temperatureEnabled = new ValidatedBoolean(true);

	public ValidatedBoolean immersiveWeatheringStormsEnabled = new ValidatedBoolean(true);

	public ValidatedBoolean dangerousHeatTemperature = new ValidatedBoolean(true);

	public ValidatedBoolean dangerousColdTemperature = new ValidatedBoolean(true);

	public ValidatedDouble goldFernChance = new ValidatedDouble(0.01, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble onFireModifier = new ValidatedDouble(12.5, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble sprintModifier = new ValidatedDouble(1.5, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble altitudeModifier = new ValidatedDouble(-6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean temperatureImmunityOnDeathEnabled = new ValidatedBoolean(true);

	public ValidatedInt temperatureImmunityOnDeathTime = new ValidatedInt(1800, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean temperatureImmunityOnFirstSpawnEnabled = new ValidatedBoolean(true);

	public ValidatedInt temperatureImmunityOnFirstSpawnTime = new ValidatedInt(1800, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean heatTemperatureSecondaryEffects = new ValidatedBoolean(true);

	public ValidatedBoolean coldTemperatureSecondaryEffects = new ValidatedBoolean(true);

	public ValidatedDouble heatThirstEffectModifier = new ValidatedDouble(0.2d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble coldHungerEffectModifier = new ValidatedDouble(0.1d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean wetnessEnabled = new ValidatedBoolean(true);

	public ValidatedList<String> wetnessImmunityMounts = ValidatedList.ofString(List.of("alexscaves:submarine", "immersive_machinery:bamboo_bee", "immersive_machinery:tunnel_digger", "immersive_machinery:redstone_sheep", "immersive_machinery:copperfin"));

	public ValidatedDouble wetMultiplier = new ValidatedDouble(-10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble wetCoolingRateMultiplier = new ValidatedDouble(1.5, 100.0, 0.1, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt wetnessTickTimer = new ValidatedInt(10, 100000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt wetnessDecrease = new ValidatedInt(-3, 0, -1000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt wetnessRainIncrease = new ValidatedInt(7, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt wetnessFluidIncrease = new ValidatedInt(10, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble playerHuddlingModifier = new ValidatedDouble(0.5, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt playerHuddlingRadius = new ValidatedInt(1, 10, 0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble biomeTemperatureMultiplier = new ValidatedDouble(18.0d, 1000.0, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean biomeEffectsEnabled = new ValidatedBoolean(true);

	public ValidatedDouble biomeDrynessMultiplier = new ValidatedDouble(0.2d, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble undergroundBiomeTemperatureMultiplier = new ValidatedDouble(0.8d, 1000.0, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt undergroundEffectStartDistanceToWS = new ValidatedInt(10, 400, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt undergroundEffectEndDistanceToWS = new ValidatedInt(16, 400, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble rainTemperatureModifier = new ValidatedDouble(-2.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble snowTemperatureModifier = new ValidatedDouble(-6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean vanillaFreezeEnabled = new ValidatedBoolean(false);

	public ValidatedDouble maxFreezeTemperatureModifier = new ValidatedDouble(-10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt maxFreezeEffectTick = new ValidatedInt(400, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble timeModifier = new ValidatedDouble(2.0d, Double.POSITIVE_INFINITY, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble biomeTimeMultiplier = new ValidatedDouble(1.75d, Double.POSITIVE_INFINITY, 1.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble shadeTimeModifier = new ValidatedDouble(-6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble shadeTimeModifierThreshold = new ValidatedDouble(9.0, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble heatingCoat1Modifier = new ValidatedDouble(2.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble heatingCoat2Modifier = new ValidatedDouble(3.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble heatingCoat3Modifier = new ValidatedDouble(4.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble coolingCoat1Modifier = new ValidatedDouble(2.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble coolingCoat2Modifier = new ValidatedDouble(3.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble coolingCoat3Modifier = new ValidatedDouble(4.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble thermalCoat1Modifier = new ValidatedDouble(2.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble thermalCoat2Modifier = new ValidatedDouble(3.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble thermalCoat3Modifier = new ValidatedDouble(4.0d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt tempInfluenceMaximumDist = new ValidatedInt(20, 40, 1, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble tempInfluenceUpDistMultiplier = new ValidatedDouble(0.75, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble tempInfluenceInWaterDistMultiplier = new ValidatedDouble(0.25, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble tempInfluenceOutsideDistMultiplier = new ValidatedDouble(0.5, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedInt tempTickTime = new ValidatedInt(20, Integer.MAX_VALUE, 5, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble maxTemperatureModification = new ValidatedDouble(1.0, Integer.MAX_VALUE, 0.1, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble minTemperatureModification = new ValidatedDouble(0.2, Integer.MAX_VALUE, 0.1, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble tfcItemHeatMultiplier = new ValidatedDouble(0.01d, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble tfcTemperatureMultiplier = new ValidatedDouble(1.0d, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);
}
