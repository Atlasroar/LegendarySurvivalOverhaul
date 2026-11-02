package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedEnum;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

/**
 * Client settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class ClientConfig extends ConfigData
{
	public ClientConfig()
	{
		super("client");
	}

	public ValidatedBoolean foodSaturationDisplayed = new ValidatedBoolean(true);

	public ValidatedBoolean showVanillaBarAnimationOverlay = new ValidatedBoolean(true);

	public ValidatedEnum<EnumUtil.temperatureDisplayMode> temperatureDisplayMode = new ValidatedEnum<>(EnumUtil.temperatureDisplayMode.SYMBOL);

	public ValidatedInt temperatureDisplayOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt temperatureDisplayOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bodyTemperatureDisplayOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bodyTemperatureDisplayOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean heatTemperatureOverlay = new ValidatedBoolean(true);

	public ValidatedBoolean coldTemperatureOverlay = new ValidatedBoolean(true);

	public ValidatedBoolean breathingSoundEnabled = new ValidatedBoolean(true);

	public ValidatedDouble coldBreathEffectThreshold = new ValidatedDouble(10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean renderTemperatureInFahrenheit = new ValidatedBoolean(false);

	public ValidatedInt wetnessIndicatorOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt wetnessIndicatorOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bodyDamageIndicatorOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bodyDamageIndicatorOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble bodyDamageIndicatorRenderHealthLimit = new ValidatedDouble(1.0, 1.1, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedInt seasonCardsDisplayOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt seasonCardsDisplayOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt seasonCardsSpawnDimensionDelayInTicks = new ValidatedInt(80, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt seasonCardsDisplayTimeInTicks = new ValidatedInt(40, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt seasonCardsFadeInInTicks = new ValidatedInt(20, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt seasonCardsFadeOutInTicks = new ValidatedInt(20, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean showHydrationTooltip = new ValidatedBoolean(true);

	public ValidatedBoolean mergeHydrationAndSaturationTooltip = new ValidatedBoolean(true);

	public ValidatedBoolean hydrationSaturationDisplayed = new ValidatedBoolean(true);

	public ValidatedBoolean lowHydrationEffect = new ValidatedBoolean(true);

	public ValidatedBoolean showHydrationBar = new ValidatedBoolean(true);

	public ValidatedBoolean showHydrationExhaustion = new ValidatedBoolean(true);

	public ValidatedBoolean showDrinkPreview = new ValidatedBoolean(true);

	public ValidatedInt hydrationBarOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt hydrationBarOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean appendBrokenShieldHeartsToHealthBar = new ValidatedBoolean(true);
}
