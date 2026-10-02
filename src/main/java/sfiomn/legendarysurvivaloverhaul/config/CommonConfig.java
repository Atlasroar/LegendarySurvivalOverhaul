package sfiomn.legendarysurvivaloverhaul.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

/**
 * Common settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class CommonConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public CommonConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "common"));
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

	public ConfigGroup group_core = new ConfigGroup("group_core");

	@Comment("How the mod represents a challenge for the player. Accepted values are as follows: PEACEFUL - Temperature doesn't harm the player and the hydration doesn't decrease. EASY - Temperature and Thirst won't hurt the player health below 10 hearts. NORMAL - Temperature and Thirst hurts the player down to 1 heart. HARD - Temperature and Thirst can kill the player. Any other value will default to HARD.")
	@ConfigGroup.Pop
	public ValidatedEnum<EnumUtil.DifficultyMode> difficultyMode = new ValidatedEnum<>(EnumUtil.DifficultyMode.HARD);

	public ConfigGroup group_core_advanced = new ConfigGroup("group_core_advanced");

	@Comment("How often player temperature, thirst, body damage and health is regularly synced between the client and server, in ticks. Lower values will increase accuracy at the cost of performance.")
	@ConfigGroup.Pop
	public ValidatedInt routinePacketSync = new ValidatedInt(30, Integer.MAX_VALUE, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_misc = new ConfigGroup("group_misc");

	@Comment("What information the compass returns when player is using it or in an item frame.")
	public ValidatedEnum<EnumUtil.CompassInfo> compassInfoMode = new ValidatedEnum<>(EnumUtil.CompassInfo.FULL);

	@Comment("If enabled, use on a filled map will show destination coordinates.")
	public ValidatedBoolean showCoordinateOnMap = new ValidatedBoolean(true);

	@Comment("If enabled, information like position and direction will be hidden from the debug screen (F3).")
	@ConfigGroup.Pop
	public ValidatedBoolean hideInfoFromDebug = new ValidatedBoolean(true);

	public ConfigGroup group_food = new ConfigGroup("group_food");

	@Comment("Food exhausted every 10 ticks. Increase the base minecraft food exhaustion.")
	public ValidatedDouble baseFoodExhaustion = new ValidatedDouble(0.05d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Food exhausted every 10 ticks while sprinting in addition to the sprinting minecraft food exhaustion.")
	public ValidatedDouble sprintingFoodExhaustion = new ValidatedDouble(0.1d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Food exhausted on every attack in addition to the minecraft attack food exhaustion.")
	@ConfigGroup.Pop
	public ValidatedDouble onAttackFoodExhaustion = new ValidatedDouble(0.1d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

}
