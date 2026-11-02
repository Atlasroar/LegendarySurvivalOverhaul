package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedEnum;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

/**
 * Common settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class CommonConfig extends ConfigData
{
	public CommonConfig()
	{
		super("common");
	}

	public ValidatedEnum<EnumUtil.DifficultyMode> difficultyMode = new ValidatedEnum<>(EnumUtil.DifficultyMode.HARD);

	public ValidatedInt routinePacketSync = new ValidatedInt(30, Integer.MAX_VALUE, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedEnum<EnumUtil.CompassInfo> compassInfoMode = new ValidatedEnum<>(EnumUtil.CompassInfo.FULL);

	public ValidatedBoolean showCoordinateOnMap = new ValidatedBoolean(true);

	public ValidatedBoolean hideInfoFromDebug = new ValidatedBoolean(true);

	public ValidatedDouble baseFoodExhaustion = new ValidatedDouble(0.05d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble sprintingFoodExhaustion = new ValidatedDouble(0.1d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble onAttackFoodExhaustion = new ValidatedDouble(0.1d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

}
