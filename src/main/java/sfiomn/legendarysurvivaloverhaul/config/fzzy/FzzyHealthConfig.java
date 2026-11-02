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
 * Health settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class FzzyHealthConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public FzzyHealthConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "health"));
	}

	@Override
	public void onSyncClient()
	{
		FzzyConfigBridge.bake(this, "health");
	}

	@Override
	public void onSyncServer()
	{
		FzzyConfigBridge.bake(this, "health");
	}

	@Override
	public void onUpdateClient()
	{
		FzzyConfigBridge.bake(this, "health");
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		FzzyConfigBridge.bake(this, "health");
	}

	@Comment("Whether the overhaul health system is enabled.")
	public ValidatedBoolean healthOverhaulEnabled = FzzyConfigBridge.field("health", "healthOverhaulEnabled");

	@Comment("How much health player will have initially.")
	public ValidatedDouble initialHealth = FzzyConfigBridge.field("health", "initialHealth");

	@Comment("How much of Additional Health a player can accumulate. 2 Heath means a full heart.")
	public ValidatedDouble maxAdditionalHealth = FzzyConfigBridge.field("health", "maxAdditionalHealth");

	@Comment("How much health ratio are recovered from bed sleeping.")
	public ValidatedDouble healthRatioRecoveredFromSleep = FzzyConfigBridge.field("health", "healthRatioRecoveredFromSleep");

	public ConfigGroup group_regeneration = new ConfigGroup("group_regeneration");

	@Comment("If enabled, the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)")
	public ValidatedBoolean naturalRegenerationEnabled = FzzyConfigBridge.field("health", "naturalRegenerationEnabled");

	@Comment("Enable custom health regeneration when natural regen is off. Consumes saturation and hunger.")
	public ValidatedBoolean customHealthRegenEnabled = FzzyConfigBridge.field("health", "customHealthRegenEnabled");

	@Comment("Amount of health to regenerate per tick rate.")
	public ValidatedDouble customHealthRegenRate = FzzyConfigBridge.field("health", "customHealthRegenRate");

	@Comment("How often in ticks health regenerates. 20 ticks = 1s")
	public ValidatedInt customHealthRegenTickRate = FzzyConfigBridge.field("health", "customHealthRegenTickRate");

	@Comment("Food exhaustion per health point regenerated.")
	@ConfigGroup.Pop
	public ValidatedDouble customHealthRegenFoodExhaustion = FzzyConfigBridge.field("health", "customHealthRegenFoodExhaustion");

	public ConfigGroup group_shield_health = new ConfigGroup("group_shield_health");

	@Comment("How much of Shield Health a player can accumulate. 2 Shield Heath means a full shield. Shield Health are lost when the player suffers damages and can't regenerate. Works similarly as the Minecraft Absorption.")
	public ValidatedDouble maxShieldHealth = FzzyConfigBridge.field("health", "maxShieldHealth");

	@Comment("Override the absorption effect by a shield health increase of 2. The absorption is typically given by the Golden Apple.")
	@ConfigGroup.Pop
	public ValidatedBoolean absorptionEffectOverride = FzzyConfigBridge.field("health", "absorptionEffectOverride");

	public ConfigGroup group_enchanted_golden_apple = new ConfigGroup("group_enchanted_golden_apple");

	@Comment("Whether eating an Enchanted Golden Apple specifically grants Shield Health and repairs Heart Containers, instead of the generic Absorption Override above. Requires Absorption Override to be enabled.")
	public ValidatedBoolean enchantedGoldenAppleOverrideEnabled = FzzyConfigBridge.field("health", "enchantedGoldenAppleOverrideEnabled");

	@Comment("How much Shield Health is granted when eating an Enchanted Golden Apple. 2 Shield Health means a full Shield Heart.")
	public ValidatedDouble enchantedGoldenAppleShieldHealth = FzzyConfigBridge.field("health", "enchantedGoldenAppleShieldHealth");

	@Comment("How many Heart Containers are repaired (restored) when eating an Enchanted Golden Apple.")
	@ConfigGroup.Pop
	public ValidatedInt enchantedGoldenAppleHeartContainersRepaired = FzzyConfigBridge.field("health", "enchantedGoldenAppleHeartContainersRepaired");

	public ConfigGroup group_heart_loss = new ConfigGroup("group_heart_loss");

	@Comment("The number of Hearts lost on death.")
	public ValidatedInt heartsLostOnDeath = FzzyConfigBridge.field("health", "heartsLostOnDeath");

	@Comment("The number of Hearts below which player can't lose hearts upon death. The hearts below this limit are de facto Permanent Hearts. A value of 1 means a player can be brought as low as 1 Heart Container (2 Health) from repeated deaths, but never lower, and is never forced into Spectator mode.")
	@ConfigGroup.Pop
	public ValidatedInt permanentHearts = FzzyConfigBridge.field("health", "permanentHearts");

	public ConfigGroup group_broken_hearts = new ConfigGroup("group_broken_hearts");

	@Comment("The Resilient Hearts is the number of heart below which Broken Hearts can no longer be added. By default, the player has 2 resilient heart, meaning no matter the amount of broken hearts, the player won't go below 2 hearts.")
	public ValidatedInt resilientHeartsWithBrokenHearts = FzzyConfigBridge.field("health", "resilientHeartsWithBrokenHearts");

	@Comment("Amount of Broken Hearts added per limbs fully injured.")
	public ValidatedDouble brokenHeartsPerInjuredLimb = FzzyConfigBridge.field("health", "brokenHeartsPerInjuredLimb");

	@Comment("How broken hearts inflicted per injured limbs are calculated. The total amount will be round down to have an integer amount of broken hearts. For example, if the amount per injured limb is 0.1 with mode Player Dynamic and the player has 3 limbs injured, the total amount is 3 * (0.1 * 20), 20 being the default player max health, so 6 broken hearts will be inflicted. Accepted values are as follows: SIMPLE - The broken heart amount is a fixed value defined in Broken Hearts Per Injured Limb. PLAYER_DYNAMIC - The broken heart amount is a percentage value of the player max health using the percentage value defined in Broken Hearts Per Injured Limb. LIMB_DYNAMIC - The broken heart amount is a percentage value of the injured limb max health using the percentage value defined in Broken Hearts Per Injured Limb. Any other value will default to SIMPLE.")
	@ConfigGroup.Pop
	public ValidatedEnum<EnumUtil.brokenHeartsPerInjuredLimbMode> brokenHeartsPerInjuredLimbMode = FzzyConfigBridge.field("health", "brokenHeartsPerInjuredLimbMode");
}
