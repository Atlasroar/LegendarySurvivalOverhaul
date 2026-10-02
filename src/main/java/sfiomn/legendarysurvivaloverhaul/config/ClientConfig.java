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
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;
import java.util.Collections;
import java.util.List;

/**
 * Client settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class ClientConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public ClientConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "client"));
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

	public ConfigGroup group_hud_general = new ConfigGroup("group_hud_general");

	@Comment("If enabled, the food saturation will be rendered on the Food Bar while the player suffers Cold Hunger Effect (secondary temperature effect).")
	public ValidatedBoolean foodSaturationDisplayed = new ValidatedBoolean(true);

	@Comment("Whether the vanilla animation of the Food bar and Hydration bar is rendered. The bar shakes more the lower they are. This mod render a new food bar as a secondary effect of a cold temperature. Disable this animation if the temperature secondary effect is enabled to allow a compatibility with other mods rendering the food bar (for example Appleskin).")
	@ConfigGroup.Pop
	public ValidatedBoolean showVanillaBarAnimationOverlay = new ValidatedBoolean(true);

	public ConfigGroup group_hud_temperature = new ConfigGroup("group_hud_temperature");

	@Comment("How temperature is displayed. Accepted values are as follows: SYMBOL - Display the player's current temperature as a symbol above the hotbar. NONE - Disable the temperature indicator.")
	public ValidatedEnum<EnumUtil.temperatureDisplayMode> temperatureDisplayMode = new ValidatedEnum<>(EnumUtil.temperatureDisplayMode.SYMBOL);

	@Comment("The X and Y offset of the temperature indicator. Set both to 0 for no offset.")
	public ValidatedInt temperatureDisplayOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt temperatureDisplayOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("The X and Y offset of the body temperature, shown when the thermometer is equipped as a Trinkets accessory. Set both to 0 for no offset.")
	public ValidatedInt bodyTemperatureDisplayOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bodyTemperatureDisplayOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("If enabled, player will see a foggy effect when the heat is high.")
	public ValidatedBoolean heatTemperatureOverlay = new ValidatedBoolean(true);

	@Comment("If enabled, player will see a frost effect when the cold is low.")
	public ValidatedBoolean coldTemperatureOverlay = new ValidatedBoolean(true);

	@Comment("If enabled, breathing sound can be heard while player faces harsh temperatures.")
	public ValidatedBoolean breathingSoundEnabled = new ValidatedBoolean(true);

	@Comment("Temperature threshold below which a cold breath effect is rendered by the player. -1000 disable the feature.")
	public ValidatedDouble coldBreathEffectThreshold = new ValidatedDouble(10.0, 1000.0, -1000.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("If enabled, render the temperature values in Fahrenheit.")
	@ConfigGroup.Pop
	public ValidatedBoolean renderTemperatureInFahrenheit = new ValidatedBoolean(false);

	public ConfigGroup group_hud_temperature_wetness = new ConfigGroup("group_hud_temperature_wetness");

	@Comment("The X and Y offset of the wetness indicator. Set both to 0 for no offset.")
	public ValidatedInt wetnessIndicatorOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@ConfigGroup.Pop
	public ValidatedInt wetnessIndicatorOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_body_damage = new ConfigGroup("group_body_damage");

	@Comment("The X and Y offset of the body damage indicator. Set both to 0 for no offset. By default, render next to the inventory bar.")
	public ValidatedInt bodyDamageIndicatorOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bodyDamageIndicatorOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Limb health threshold below which the body damage indicator is rendered. If set to 1.1, the body damage indicator is always rendered. If set to 1.0, the body damage indicator is rendered as soon as a limb is wounded.")
	@ConfigGroup.Pop
	public ValidatedDouble bodyDamageIndicatorRenderHealthLimit = new ValidatedDouble(1.0, 1.1, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_season_cards = new ConfigGroup("group_season_cards");

	@Comment("The X and Y offset of the season cards. Set both to 0 for no offset. By default, render first top quarter vertically and centered horizontally.")
	public ValidatedInt seasonCardsDisplayOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt seasonCardsDisplayOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("The delay before rendering the season card at first player spawn or player dimension change.")
	public ValidatedInt seasonCardsSpawnDimensionDelayInTicks = new ValidatedInt(80, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("The display time in ticks that the season card will be fully rendered.")
	public ValidatedInt seasonCardsDisplayTimeInTicks = new ValidatedInt(40, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("The fade in time in ticks that the season card will appear.")
	public ValidatedInt seasonCardsFadeInInTicks = new ValidatedInt(20, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("The fade out time in ticks that the season card will disappear.")
	@ConfigGroup.Pop
	public ValidatedInt seasonCardsFadeOutInTicks = new ValidatedInt(20, Integer.MAX_VALUE, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_thirst_tooltip = new ConfigGroup("group_thirst_tooltip");

	@Comment("If enabled, show the hydration values in the item tooltip.")
	public ValidatedBoolean showHydrationTooltip = new ValidatedBoolean(true);

	@Comment("If enabled, show the hydration and the saturation values on the same line in the tooltip.")
	@ConfigGroup.Pop
	public ValidatedBoolean mergeHydrationAndSaturationTooltip = new ValidatedBoolean(true);

	public ConfigGroup group_thirst = new ConfigGroup("group_thirst");

	@Comment("Whether the Hydration Saturation is displayed or not.")
	public ValidatedBoolean hydrationSaturationDisplayed = new ValidatedBoolean(true);

	@Comment("If enabled, player's vision will become blurry when running low on hydration.")
	@ConfigGroup.Pop
	public ValidatedBoolean lowHydrationEffect = new ValidatedBoolean(true);

	public ConfigGroup group_thirst_hydration_bar = new ConfigGroup("group_thirst_hydration_bar");

	@Comment("If enabled, the hydration bar will be displayed.")
	public ValidatedBoolean showHydrationBar = new ValidatedBoolean(true);

	@Comment("If enabled, the Hydration Exhaustion will be displayed (grey bar behind the hydration bar).")
	public ValidatedBoolean showHydrationExhaustion = new ValidatedBoolean(true);

	@Comment("If enabled, a preview on hydration bar will be displayed if player holds a drink item.")
	public ValidatedBoolean showDrinkPreview = new ValidatedBoolean(true);

	@Comment("How much the hydration bar is moved to the right.")
	public ValidatedInt hydrationBarOffsetX = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("How much the hydration bar is moved to the bottom.")
	@ConfigGroup.Pop
	public ValidatedInt hydrationBarOffsetY = new ValidatedInt(0, 10000, -10000, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_health_overhaul = new ConfigGroup("group_health_overhaul");

	@Comment("If enabled, renders broken hearts in the unused health slots. Shield hearts use a separate row and move the armor row when present.")
	@ConfigGroup.Pop
	public ValidatedBoolean appendBrokenShieldHeartsToHealthBar = new ValidatedBoolean(true);
}
