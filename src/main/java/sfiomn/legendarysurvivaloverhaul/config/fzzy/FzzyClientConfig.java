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
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

/**
 * Client settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class FzzyClientConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public FzzyClientConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "client"));
	}

	@Override
	public void onSyncClient()
	{
		FzzyConfigBridge.bake(this, "client");
	}

	@Override
	public void onSyncServer()
	{
		FzzyConfigBridge.bake(this, "client");
	}

	@Override
	public void onUpdateClient()
	{
		FzzyConfigBridge.bake(this, "client");
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		FzzyConfigBridge.bake(this, "client");
	}

	public ConfigGroup group_hud_general = new ConfigGroup("group_hud_general");

	@Comment("If enabled, the food saturation will be rendered on the Food Bar while the player suffers Cold Hunger Effect (secondary temperature effect).")
	public ValidatedBoolean foodSaturationDisplayed = FzzyConfigBridge.field("client", "foodSaturationDisplayed");

	@Comment("Whether the vanilla animation of the Food bar and Hydration bar is rendered. The bar shakes more the lower they are. This mod render a new food bar as a secondary effect of a cold temperature. Disable this animation if the temperature secondary effect is enabled to allow a compatibility with other mods rendering the food bar (for example Appleskin).")
	@ConfigGroup.Pop
	public ValidatedBoolean showVanillaBarAnimationOverlay = FzzyConfigBridge.field("client", "showVanillaBarAnimationOverlay");

	public ConfigGroup group_hud_temperature = new ConfigGroup("group_hud_temperature");

	@Comment("How temperature is displayed. Accepted values are as follows: SYMBOL - Display the player's current temperature as a symbol above the hotbar. NONE - Disable the temperature indicator.")
	public ValidatedEnum<EnumUtil.temperatureDisplayMode> temperatureDisplayMode = FzzyConfigBridge.field("client", "temperatureDisplayMode");

	@Comment("The X and Y offset of the temperature indicator. Set both to 0 for no offset.")
	public ValidatedInt temperatureDisplayOffsetX = FzzyConfigBridge.field("client", "temperatureDisplayOffsetX");

	public ValidatedInt temperatureDisplayOffsetY = FzzyConfigBridge.field("client", "temperatureDisplayOffsetY");

	@Comment("The X and Y offset of the body temperature, shown when the thermometer is equipped as a Trinkets accessory. Set both to 0 for no offset.")
	public ValidatedInt bodyTemperatureDisplayOffsetX = FzzyConfigBridge.field("client", "bodyTemperatureDisplayOffsetX");

	public ValidatedInt bodyTemperatureDisplayOffsetY = FzzyConfigBridge.field("client", "bodyTemperatureDisplayOffsetY");

	@Comment("If enabled, player will see a foggy effect when the heat is high.")
	public ValidatedBoolean heatTemperatureOverlay = FzzyConfigBridge.field("client", "heatTemperatureOverlay");

	@Comment("If enabled, player will see a frost effect when the cold is low.")
	public ValidatedBoolean coldTemperatureOverlay = FzzyConfigBridge.field("client", "coldTemperatureOverlay");

	@Comment("If enabled, breathing sound can be heard while player faces harsh temperatures.")
	public ValidatedBoolean breathingSoundEnabled = FzzyConfigBridge.field("client", "breathingSoundEnabled");

	@Comment("Temperature threshold below which a cold breath effect is rendered by the player. -1000 disable the feature.")
	public ValidatedDouble coldBreathEffectThreshold = FzzyConfigBridge.field("client", "coldBreathEffectThreshold");

	@Comment("If enabled, render the temperature values in Fahrenheit.")
	@ConfigGroup.Pop
	public ValidatedBoolean renderTemperatureInFahrenheit = FzzyConfigBridge.field("client", "renderTemperatureInFahrenheit");

	public ConfigGroup group_hud_temperature_wetness = new ConfigGroup("group_hud_temperature_wetness");

	@Comment("The X and Y offset of the wetness indicator. Set both to 0 for no offset.")
	public ValidatedInt wetnessIndicatorOffsetX = FzzyConfigBridge.field("client", "wetnessIndicatorOffsetX");

	@ConfigGroup.Pop
	public ValidatedInt wetnessIndicatorOffsetY = FzzyConfigBridge.field("client", "wetnessIndicatorOffsetY");

	public ConfigGroup group_body_damage = new ConfigGroup("group_body_damage");

	@Comment("The X and Y offset of the body damage indicator. Set both to 0 for no offset. By default, render next to the inventory bar.")
	public ValidatedInt bodyDamageIndicatorOffsetX = FzzyConfigBridge.field("client", "bodyDamageIndicatorOffsetX");

	public ValidatedInt bodyDamageIndicatorOffsetY = FzzyConfigBridge.field("client", "bodyDamageIndicatorOffsetY");

	@Comment("Limb health threshold below which the body damage indicator is rendered. If set to 1.1, the body damage indicator is always rendered. If set to 1.0, the body damage indicator is rendered as soon as a limb is wounded.")
	@ConfigGroup.Pop
	public ValidatedDouble bodyDamageIndicatorRenderHealthLimit = FzzyConfigBridge.field("client", "bodyDamageIndicatorRenderHealthLimit");

	public ConfigGroup group_season_cards = new ConfigGroup("group_season_cards");

	@Comment("The X and Y offset of the season cards. Set both to 0 for no offset. By default, render first top quarter vertically and centered horizontally.")
	public ValidatedInt seasonCardsDisplayOffsetX = FzzyConfigBridge.field("client", "seasonCardsDisplayOffsetX");

	public ValidatedInt seasonCardsDisplayOffsetY = FzzyConfigBridge.field("client", "seasonCardsDisplayOffsetY");

	@Comment("The delay before rendering the season card at first player spawn or player dimension change.")
	public ValidatedInt seasonCardsSpawnDimensionDelayInTicks = FzzyConfigBridge.field("client", "seasonCardsSpawnDimensionDelayInTicks");

	@Comment("The display time in ticks that the season card will be fully rendered.")
	public ValidatedInt seasonCardsDisplayTimeInTicks = FzzyConfigBridge.field("client", "seasonCardsDisplayTimeInTicks");

	@Comment("The fade in time in ticks that the season card will appear.")
	public ValidatedInt seasonCardsFadeInInTicks = FzzyConfigBridge.field("client", "seasonCardsFadeInInTicks");

	@Comment("The fade out time in ticks that the season card will disappear.")
	@ConfigGroup.Pop
	public ValidatedInt seasonCardsFadeOutInTicks = FzzyConfigBridge.field("client", "seasonCardsFadeOutInTicks");

	public ConfigGroup group_thirst_tooltip = new ConfigGroup("group_thirst_tooltip");

	@Comment("If enabled, show the hydration values in the item tooltip.")
	public ValidatedBoolean showHydrationTooltip = FzzyConfigBridge.field("client", "showHydrationTooltip");

	@Comment("If enabled, show the hydration and the saturation values on the same line in the tooltip.")
	@ConfigGroup.Pop
	public ValidatedBoolean mergeHydrationAndSaturationTooltip = FzzyConfigBridge.field("client", "mergeHydrationAndSaturationTooltip");

	public ConfigGroup group_thirst = new ConfigGroup("group_thirst");

	@Comment("Whether the Hydration Saturation is displayed or not.")
	public ValidatedBoolean hydrationSaturationDisplayed = FzzyConfigBridge.field("client", "hydrationSaturationDisplayed");

	@Comment("If enabled, player's vision will become blurry when running low on hydration.")
	@ConfigGroup.Pop
	public ValidatedBoolean lowHydrationEffect = FzzyConfigBridge.field("client", "lowHydrationEffect");

	public ConfigGroup group_thirst_hydration_bar = new ConfigGroup("group_thirst_hydration_bar");

	@Comment("If enabled, the hydration bar will be displayed.")
	public ValidatedBoolean showHydrationBar = FzzyConfigBridge.field("client", "showHydrationBar");

	@Comment("If enabled, the Hydration Exhaustion will be displayed (grey bar behind the hydration bar).")
	public ValidatedBoolean showHydrationExhaustion = FzzyConfigBridge.field("client", "showHydrationExhaustion");

	@Comment("If enabled, a preview on hydration bar will be displayed if player holds a drink item.")
	public ValidatedBoolean showDrinkPreview = FzzyConfigBridge.field("client", "showDrinkPreview");

	@Comment("How much the hydration bar is moved to the right.")
	public ValidatedInt hydrationBarOffsetX = FzzyConfigBridge.field("client", "hydrationBarOffsetX");

	@Comment("How much the hydration bar is moved to the bottom.")
	@ConfigGroup.Pop
	public ValidatedInt hydrationBarOffsetY = FzzyConfigBridge.field("client", "hydrationBarOffsetY");

	public ConfigGroup group_health_overhaul = new ConfigGroup("group_health_overhaul");

	@Comment("If enabled, renders broken hearts in the unused health slots. Shield hearts use a separate row and move the armor row when present.")
	@ConfigGroup.Pop
	public ValidatedBoolean appendBrokenShieldHeartsToHealthBar = FzzyConfigBridge.field("client", "appendBrokenShieldHeartsToHealthBar");
}
