package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedList;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import java.util.List;

/**
 * Seasons settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class SeasonsConfig extends ConfigData
{
	public SeasonsConfig()
	{
		super("seasons");
	}

	public ValidatedBoolean sereneSeasonsEnabled = new ValidatedBoolean(true);

	public ValidatedBoolean ssTropicalSeasonsEnabled = new ValidatedBoolean(false);

	public ValidatedBoolean ssSeasonCardsEnabled = new ValidatedBoolean(false);

	public ValidatedBoolean ssDefaultSeasonEnabled = new ValidatedBoolean(true);

	public ValidatedDouble ssEarlySpringModifier = new ValidatedDouble(-3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidSpringModifier = new ValidatedDouble(0.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssLateSpringModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssEarlySummerModifier = new ValidatedDouble(6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidSummerModifier = new ValidatedDouble(10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssLateSummerModifier = new ValidatedDouble(6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssEarlyAutumnModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidAutumnModifier = new ValidatedDouble(0.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssLateAutumnModifier = new ValidatedDouble(-3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssEarlyWinterModifier = new ValidatedDouble(-7.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidWinterModifier = new ValidatedDouble(-12.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssLateWinterModifier = new ValidatedDouble(-7.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssEarlyWetSeasonModifier = new ValidatedDouble(-1.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidWetSeasonModifier = new ValidatedDouble(-5.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssLateWetSeasonModifier = new ValidatedDouble(-1.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssEarlyDrySeasonModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidDrySeasonModifier = new ValidatedDouble(7.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssLateDrySeasonModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean eclipticSeasonsEnabled = new ValidatedBoolean(true);

	public ValidatedList<Double> esSpringModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(-10.0, -7.0, -5.0, -3.0, -1.0, 0.0));

	public ValidatedList<Double> esSummerModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(1.0, 3.0, 5.0, 7.0, 9.0, 10.0));

	public ValidatedList<Double> esAutumnModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(9.0, 7.0, 5.0, 3.0, 1.0, 0.0));

	public ValidatedList<Double> esWinterModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(-1.0, -3.0, -5.0, -7.0, -10.0, -12.0));
}
