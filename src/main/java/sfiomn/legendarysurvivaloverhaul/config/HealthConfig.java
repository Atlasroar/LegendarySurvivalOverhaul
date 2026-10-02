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
 * Health settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class HealthConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public HealthConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "health"));
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

	@Comment("Whether the overhaul health system is enabled.")
	public ValidatedBoolean healthOverhaulEnabled = new ValidatedBoolean(true);

	@Comment("How much health player will have initially.")
	public ValidatedDouble initialHealth = new ValidatedDouble(20.0, 10000.0, 1.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much of Additional Health a player can accumulate. 2 Heath means a full heart.")
	public ValidatedDouble maxAdditionalHealth = new ValidatedDouble(20.0, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much health ratio are recovered from bed sleeping.")
	public ValidatedDouble healthRatioRecoveredFromSleep = new ValidatedDouble(1.0d, 1.0d, 0.0d, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_regeneration = new ConfigGroup("group_regeneration");

	@Comment("If enabled, the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)")
	public ValidatedBoolean naturalRegenerationEnabled = new ValidatedBoolean(false);

	@Comment("Enable custom health regeneration when natural regen is off. Consumes saturation and hunger.")
	public ValidatedBoolean customHealthRegenEnabled = new ValidatedBoolean(true);

	@Comment("Amount of health to regenerate per tick rate.")
	public ValidatedDouble customHealthRegenRate = new ValidatedDouble(1.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How often in ticks health regenerates. 20 ticks = 1s")
	public ValidatedInt customHealthRegenTickRate = new ValidatedInt(200, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Food exhaustion per health point regenerated.")
	@ConfigGroup.Pop
	public ValidatedDouble customHealthRegenFoodExhaustion = new ValidatedDouble(6.0, 100.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_shield_health = new ConfigGroup("group_shield_health");

	@Comment("How much of Shield Health a player can accumulate. 2 Shield Heath means a full shield. Shield Health are lost when the player suffers damages and can't regenerate. Works similarly as the Minecraft Absorption.")
	public ValidatedDouble maxShieldHealth = new ValidatedDouble(20.0, 10000.0, 1.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Override the absorption effect by a shield health increase of 2. The absorption is typically given by the Golden Apple.")
	@ConfigGroup.Pop
	public ValidatedBoolean absorptionEffectOverride = new ValidatedBoolean(true);

	public ConfigGroup group_enchanted_golden_apple = new ConfigGroup("group_enchanted_golden_apple");

	@Comment("Whether eating an Enchanted Golden Apple specifically grants Shield Health and repairs Heart Containers, instead of the generic Absorption Override above. Requires Absorption Override to be enabled.")
	public ValidatedBoolean enchantedGoldenAppleOverrideEnabled = new ValidatedBoolean(true);

	@Comment("How much Shield Health is granted when eating an Enchanted Golden Apple. 2 Shield Health means a full Shield Heart.")
	public ValidatedDouble enchantedGoldenAppleShieldHealth = new ValidatedDouble(4.0, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How many Heart Containers are repaired (restored) when eating an Enchanted Golden Apple.")
	@ConfigGroup.Pop
	public ValidatedInt enchantedGoldenAppleHeartContainersRepaired = new ValidatedInt(1, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_heart_loss = new ConfigGroup("group_heart_loss");

	@Comment("The number of Hearts lost on death.")
	public ValidatedInt heartsLostOnDeath = new ValidatedInt(1, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("The number of Hearts below which player can't lose hearts upon death. The hearts below this limit are de facto Permanent Hearts. A value of 1 means a player can be brought as low as 1 Heart Container (2 Health) from repeated deaths, but never lower, and is never forced into Spectator mode.")
	@ConfigGroup.Pop
	public ValidatedInt permanentHearts = new ValidatedInt(1, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_broken_hearts = new ConfigGroup("group_broken_hearts");

	@Comment("The Resilient Hearts is the number of heart below which Broken Hearts can no longer be added. By default, the player has 2 resilient heart, meaning no matter the amount of broken hearts, the player won't go below 2 hearts.")
	public ValidatedInt resilientHeartsWithBrokenHearts = new ValidatedInt(2, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Amount of Broken Hearts added per limbs fully injured.")
	public ValidatedDouble brokenHeartsPerInjuredLimb = new ValidatedDouble(0.1, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How broken hearts inflicted per injured limbs are calculated. The total amount will be round down to have an integer amount of broken hearts. For example, if the amount per injured limb is 0.1 with mode Player Dynamic and the player has 3 limbs injured, the total amount is 3 * (0.1 * 20), 20 being the default player max health, so 6 broken hearts will be inflicted. Accepted values are as follows: SIMPLE - The broken heart amount is a fixed value defined in Broken Hearts Per Injured Limb. PLAYER_DYNAMIC - The broken heart amount is a percentage value of the player max health using the percentage value defined in Broken Hearts Per Injured Limb. LIMB_DYNAMIC - The broken heart amount is a percentage value of the injured limb max health using the percentage value defined in Broken Hearts Per Injured Limb. Any other value will default to SIMPLE.")
	@ConfigGroup.Pop
	public ValidatedEnum<EnumUtil.brokenHeartsPerInjuredLimbMode> brokenHeartsPerInjuredLimbMode = new ValidatedEnum<>(EnumUtil.brokenHeartsPerInjuredLimbMode.PLAYER_DYNAMIC);
}
