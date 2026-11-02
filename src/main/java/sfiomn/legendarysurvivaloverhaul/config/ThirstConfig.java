package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;

/**
 * Thirst settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class ThirstConfig extends ConfigData
{
	public ThirstConfig()
	{
		super("thirst");
	}

	public ValidatedBoolean thirstEnabled = new ValidatedBoolean(true);

	public ValidatedBoolean dangerousDehydration = new ValidatedBoolean(true);

	public ValidatedBoolean cumulativeThirstEffectDuration = new ValidatedBoolean(true);

	public ValidatedDouble dehydrationDamageScaling = new ValidatedDouble(0.3d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble thirstEffectModifier = new ValidatedDouble(0.25d, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble baseHydrationExhaustion = new ValidatedDouble(0.03d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble sprintingHydrationExhaustion = new ValidatedDouble(0.1d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble onJumpHydrationExhaustion = new ValidatedDouble(0.15d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble onBlockBreakHydrationExhaustion = new ValidatedDouble(0.07d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble onAttackHydrationExhaustion = new ValidatedDouble(0.3d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean selfWateringCanteenEnabled = new ValidatedBoolean(true);

	public ValidatedInt selfWateringCanteenWetnessIncrease = new ValidatedInt(400, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt canteenCapacity = new ValidatedInt(10, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt largeCanteenCapacity = new ValidatedInt(20, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean allowOverridePurifiedWater = new ValidatedBoolean(true);

	public ValidatedInt hydrationLava = new ValidatedInt(6, 20, 0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble saturationLava = new ValidatedDouble(4.0, 20.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean glassBottleLootAfterDrink = new ValidatedBoolean(true);

	public ValidatedBoolean thirstEnabledIfVampire = new ValidatedBoolean(false);
}
