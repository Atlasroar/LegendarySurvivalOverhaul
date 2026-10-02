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
 * Seasons settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class SeasonsConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public SeasonsConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "seasons"));
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

	public ConfigGroup group_serene_seasons = new ConfigGroup("group_serene_seasons");

	@Comment("If Serene Seasons is installed, whether the seasons have an effect on the player's temperature.")
	public ValidatedBoolean sereneSeasonsEnabled = new ValidatedBoolean(true);

	@Comment("If enabled, tropical biomes use Serene Seasons wet and dry seasons instead of the normal seasons.")
	public ValidatedBoolean ssTropicalSeasonsEnabled = new ValidatedBoolean(false);

	@Comment("If season cards are enabled, season cards will appear at every season changes.")
	public ValidatedBoolean ssSeasonCardsEnabled = new ValidatedBoolean(false);

	@Comment("If default season is enabled, when serene season defines no season effect in a biome, the normal season temperature will be applied. If disabled, when serene season defines no season effects, no season temperature will be applied.")
	@ConfigGroup.Pop
	public ValidatedBoolean ssDefaultSeasonEnabled = new ValidatedBoolean(true);

	public ConfigGroup group_serene_seasons_temperate_spring = new ConfigGroup("group_serene_seasons_temperate_spring");

	public ValidatedDouble ssEarlySpringModifier = new ValidatedDouble(-3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidSpringModifier = new ValidatedDouble(0.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble ssLateSpringModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_serene_seasons_temperate_summer = new ConfigGroup("group_serene_seasons_temperate_summer");

	public ValidatedDouble ssEarlySummerModifier = new ValidatedDouble(6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidSummerModifier = new ValidatedDouble(10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble ssLateSummerModifier = new ValidatedDouble(6.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_serene_seasons_temperate_autumn = new ConfigGroup("group_serene_seasons_temperate_autumn");

	public ValidatedDouble ssEarlyAutumnModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidAutumnModifier = new ValidatedDouble(0.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble ssLateAutumnModifier = new ValidatedDouble(-3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_serene_seasons_temperate_winter = new ConfigGroup("group_serene_seasons_temperate_winter");

	public ValidatedDouble ssEarlyWinterModifier = new ValidatedDouble(-7.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidWinterModifier = new ValidatedDouble(-12.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble ssLateWinterModifier = new ValidatedDouble(-7.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_serene_seasons_tropical_wet_season = new ConfigGroup("group_serene_seasons_tropical_wet_season");

	public ValidatedDouble ssEarlyWetSeasonModifier = new ValidatedDouble(-1.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidWetSeasonModifier = new ValidatedDouble(-5.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble ssLateWetSeasonModifier = new ValidatedDouble(-1.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_serene_seasons_tropical_dry_season = new ConfigGroup("group_serene_seasons_tropical_dry_season");

	public ValidatedDouble ssEarlyDrySeasonModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble ssMidDrySeasonModifier = new ValidatedDouble(7.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@ConfigGroup.Pop
	public ValidatedDouble ssLateDrySeasonModifier = new ValidatedDouble(3.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_ecliptic_seasons = new ConfigGroup("group_ecliptic_seasons");

	@Comment("If Ecliptic Seasons is installed, whether the seasons have an effect on the player's temperature.")
	@ConfigGroup.Pop
	public ValidatedBoolean eclipticSeasonsEnabled = new ValidatedBoolean(true);

	public ConfigGroup group_ecliptic_seasons_temperature = new ConfigGroup("group_ecliptic_seasons_temperature");

	public ValidatedList<Double> esSpringModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(-10.0, -7.0, -5.0, -3.0, -1.0, 0.0));

	public ValidatedList<Double> esSummerModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(1.0, 3.0, 5.0, 7.0, 9.0, 10.0));

	public ValidatedList<Double> esAutumnModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(9.0, 7.0, 5.0, 3.0, 1.0, 0.0));

	@ConfigGroup.Pop
	public ValidatedList<Double> esWinterModifier = new ValidatedDouble(0.0, Double.MAX_VALUE, -Double.MAX_VALUE).toList(List.of(-1.0, -3.0, -5.0, -7.0, -10.0, -12.0));
}
