package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import sfiomn.legendarysurvivaloverhaul.config.*;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

/**
 * Seasons settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class FzzySeasonsConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public FzzySeasonsConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "seasons"));
	}

	@Override
	public void onSyncClient()
	{
		FzzyConfigBridge.bake(this, "seasons");
	}

	@Override
	public void onSyncServer()
	{
		FzzyConfigBridge.bake(this, "seasons");
	}

	@Override
	public void onUpdateClient()
	{
		FzzyConfigBridge.bake(this, "seasons");
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		FzzyConfigBridge.bake(this, "seasons");
	}

	public ConfigGroup group_serene_seasons = new ConfigGroup("group_serene_seasons");

	@Comment("If Serene Seasons is installed, whether the seasons have an effect on the player's temperature.")
	public ValidatedBoolean sereneSeasonsEnabled = FzzyConfigBridge.field("seasons", "sereneSeasonsEnabled");

	@Comment("If enabled, tropical biomes use Serene Seasons wet and dry seasons instead of the normal seasons.")
	public ValidatedBoolean ssTropicalSeasonsEnabled = FzzyConfigBridge.field("seasons", "ssTropicalSeasonsEnabled");

	@Comment("If season cards are enabled, season cards will appear at every season changes.")
	public ValidatedBoolean ssSeasonCardsEnabled = FzzyConfigBridge.field("seasons", "ssSeasonCardsEnabled");

	@Comment("If default season is enabled, when serene season defines no season effect in a biome, the normal season temperature will be applied. If disabled, when serene season defines no season effects, no season temperature will be applied.")
	@ConfigGroup.Pop
	public ValidatedBoolean ssDefaultSeasonEnabled = FzzyConfigBridge.field("seasons", "ssDefaultSeasonEnabled");

	public ConfigGroup group_serene_seasons_temperate_spring = new ConfigGroup("group_serene_seasons_temperate_spring");

	public ValidatedDouble ssEarlySpringModifier = FzzyConfigBridge.field("seasons", "ssEarlySpringModifier");

	public ValidatedDouble ssMidSpringModifier = FzzyConfigBridge.field("seasons", "ssMidSpringModifier");

	@ConfigGroup.Pop
	public ValidatedDouble ssLateSpringModifier = FzzyConfigBridge.field("seasons", "ssLateSpringModifier");

	public ConfigGroup group_serene_seasons_temperate_summer = new ConfigGroup("group_serene_seasons_temperate_summer");

	public ValidatedDouble ssEarlySummerModifier = FzzyConfigBridge.field("seasons", "ssEarlySummerModifier");

	public ValidatedDouble ssMidSummerModifier = FzzyConfigBridge.field("seasons", "ssMidSummerModifier");

	@ConfigGroup.Pop
	public ValidatedDouble ssLateSummerModifier = FzzyConfigBridge.field("seasons", "ssLateSummerModifier");

	public ConfigGroup group_serene_seasons_temperate_autumn = new ConfigGroup("group_serene_seasons_temperate_autumn");

	public ValidatedDouble ssEarlyAutumnModifier = FzzyConfigBridge.field("seasons", "ssEarlyAutumnModifier");

	public ValidatedDouble ssMidAutumnModifier = FzzyConfigBridge.field("seasons", "ssMidAutumnModifier");

	@ConfigGroup.Pop
	public ValidatedDouble ssLateAutumnModifier = FzzyConfigBridge.field("seasons", "ssLateAutumnModifier");

	public ConfigGroup group_serene_seasons_temperate_winter = new ConfigGroup("group_serene_seasons_temperate_winter");

	public ValidatedDouble ssEarlyWinterModifier = FzzyConfigBridge.field("seasons", "ssEarlyWinterModifier");

	public ValidatedDouble ssMidWinterModifier = FzzyConfigBridge.field("seasons", "ssMidWinterModifier");

	@ConfigGroup.Pop
	public ValidatedDouble ssLateWinterModifier = FzzyConfigBridge.field("seasons", "ssLateWinterModifier");

	public ConfigGroup group_serene_seasons_tropical_wet_season = new ConfigGroup("group_serene_seasons_tropical_wet_season");

	public ValidatedDouble ssEarlyWetSeasonModifier = FzzyConfigBridge.field("seasons", "ssEarlyWetSeasonModifier");

	public ValidatedDouble ssMidWetSeasonModifier = FzzyConfigBridge.field("seasons", "ssMidWetSeasonModifier");

	@ConfigGroup.Pop
	public ValidatedDouble ssLateWetSeasonModifier = FzzyConfigBridge.field("seasons", "ssLateWetSeasonModifier");

	public ConfigGroup group_serene_seasons_tropical_dry_season = new ConfigGroup("group_serene_seasons_tropical_dry_season");

	public ValidatedDouble ssEarlyDrySeasonModifier = FzzyConfigBridge.field("seasons", "ssEarlyDrySeasonModifier");

	public ValidatedDouble ssMidDrySeasonModifier = FzzyConfigBridge.field("seasons", "ssMidDrySeasonModifier");

	@ConfigGroup.Pop
	public ValidatedDouble ssLateDrySeasonModifier = FzzyConfigBridge.field("seasons", "ssLateDrySeasonModifier");

	public ConfigGroup group_ecliptic_seasons = new ConfigGroup("group_ecliptic_seasons");

	@Comment("If Ecliptic Seasons is installed, whether the seasons have an effect on the player's temperature.")
	@ConfigGroup.Pop
	public ValidatedBoolean eclipticSeasonsEnabled = FzzyConfigBridge.field("seasons", "eclipticSeasonsEnabled");

	public ConfigGroup group_ecliptic_seasons_temperature = new ConfigGroup("group_ecliptic_seasons_temperature");

	public ValidatedList<Double> esSpringModifier = FzzyConfigBridge.field("seasons", "esSpringModifier");

	public ValidatedList<Double> esSummerModifier = FzzyConfigBridge.field("seasons", "esSummerModifier");

	public ValidatedList<Double> esAutumnModifier = FzzyConfigBridge.field("seasons", "esAutumnModifier");

	@ConfigGroup.Pop
	public ValidatedList<Double> esWinterModifier = FzzyConfigBridge.field("seasons", "esWinterModifier");
}
