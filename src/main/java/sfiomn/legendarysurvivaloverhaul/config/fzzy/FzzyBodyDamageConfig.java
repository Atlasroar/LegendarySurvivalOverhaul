package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import sfiomn.legendarysurvivaloverhaul.config.*;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

/**
 * Body Damage settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class FzzyBodyDamageConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public FzzyBodyDamageConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "body_damage"));
	}

	@Override
	public void onSyncClient()
	{
		FzzyConfigBridge.bake(this, "body_damage");
	}

	@Override
	public void onSyncServer()
	{
		FzzyConfigBridge.bake(this, "body_damage");
	}

	@Override
	public void onUpdateClient()
	{
		FzzyConfigBridge.bake(this, "body_damage");
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		FzzyConfigBridge.bake(this, "body_damage");
	}

	@Comment("Whether body members receive localized damages. The damageSourceBodyParts.json allows you to define for specific damage source, the damage spread across specified body parts. The damage distribution can either be ONE_OF or ALL. ALL means the damage are equally divided across all body parts.")
	public ValidatedBoolean localizedBodyDamageEnabled = FzzyConfigBridge.field("body_damage", "localizedBodyDamageEnabled");

	@Comment("Multiply the damage taken by the player when shot in the head without helmet.")
	public ValidatedDouble headCriticalShotMultiplier = FzzyConfigBridge.field("body_damage", "headCriticalShotMultiplier");

	@Comment("How much of the hurt player's damage is assigned to the body parts.")
	public ValidatedDouble bodyDamageMultiplier = FzzyConfigBridge.field("body_damage", "bodyDamageMultiplier");

	@Comment("How much health ratio are recovered in all body parts from bed sleeping.")
	public ValidatedDouble bodyHealthRatioRecoveredFromSleep = FzzyConfigBridge.field("body_damage", "bodyHealthRatioRecoveredFromSleep");

	@Comment("How much food is exhausted when a limb regenerates based on the amount of health regenerated. For each 1 health regenerated, the food is exhausted by this value.")
	public ValidatedDouble bodyHealingFoodExhaustion = FzzyConfigBridge.field("body_damage", "bodyHealingFoodExhaustion");

	@Comment("The hunger bar won't drop below this value while body is healing. Each hunger icon has a value of 2 in the hunger bar.")
	public ValidatedInt minFoodOnBodyHealing = FzzyConfigBridge.field("body_damage", "minFoodOnBodyHealing");

	@Comment("How long in ticks is the Addiction Effect lasting. 0 deactivates the feature. The Addiction Effect prevents you from re-using the morphine item.")
	public ValidatedInt painkillerAddictionDuration = FzzyConfigBridge.field("body_damage", "painkillerAddictionDuration");

	public ConfigGroup group_healing_limbs_passive_regeneration = new ConfigGroup("group_healing_limbs_passive_regeneration");

	@Comment("The limbs will be healed when the player is at max health.")
	public ValidatedBoolean passiveLimbRegenerationOnFullHealth = FzzyConfigBridge.field("body_damage", "passiveLimbRegenerationOnFullHealth");

	@Comment("The limbs will be healed when the player is under one of the mentioned effect, the most damaged limb first.")
	public ValidatedList<String> passiveLimbRegenerationEffects = FzzyConfigBridge.field("body_damage", "passiveLimbRegenerationEffects");

	@Comment("Whether the amplification of the effect increase the limb regeneration speed. The speed increase follows the same logic as the Regeneration Effect Amplification : Amplifier 1 -> tick timer /2, Amplifier 2 -> tick timer /4, ...")
	public ValidatedBoolean passiveLimbRegenerationAmplificationEnabled = FzzyConfigBridge.field("body_damage", "passiveLimbRegenerationAmplificationEnabled");

	@Comment("Amount of limb health regenerated, the most damaged limb first.")
	public ValidatedDouble passiveLimbHealthRegenerated = FzzyConfigBridge.field("body_damage", "passiveLimbHealthRegenerated");

	@Comment("How fast in ticks the limbs regenerate passively. 20 ticks = 1s")
	@ConfigGroup.Pop
	public ValidatedInt passiveLimbRegenerationTickTimer = FzzyConfigBridge.field("body_damage", "passiveLimbRegenerationTickTimer");

	public ConfigGroup group_healing_limbs_proportional_regeneration = new ConfigGroup("group_healing_limbs_proportional_regeneration");

	@Comment("Minimum health threshold (as ratio of max health minus broken hearts) to start regenerating limbs.")
	public ValidatedDouble proportionalLimbRegenMinThreshold = FzzyConfigBridge.field("body_damage", "proportionalLimbRegenMinThreshold");

	@Comment("Minimum limb heal value per tick rate when at min threshold. Spread proportionally across all body parts.")
	public ValidatedDouble proportionalLimbRegenMinHealValue = FzzyConfigBridge.field("body_damage", "proportionalLimbRegenMinHealValue");

	@Comment("Maximum limb heal value per tick rate when at full health. Spread proportionally across all body parts.")
	public ValidatedDouble proportionalLimbRegenMaxHealValue = FzzyConfigBridge.field("body_damage", "proportionalLimbRegenMaxHealValue");

	@Comment("How often in ticks the proportional regeneration occurs. 20 ticks = 1s")
	public ValidatedInt proportionalLimbRegenTickRate = FzzyConfigBridge.field("body_damage", "proportionalLimbRegenTickRate");

	@Comment("Rate at which healing increases from min to max. 1.0 = linear, >1 = exponential.")
	@ConfigGroup.Pop
	public ValidatedDouble proportionalLimbRegenIncreaseRate = FzzyConfigBridge.field("body_damage", "proportionalLimbRegenIncreaseRate");

	public ConfigGroup group_healing_limbs_first_aid_supplies = new ConfigGroup("group_healing_limbs_first_aid_supplies");

	@Comment("The First Aid Supplies regenerate limb health passively, either by holding it or equipping it as a trinket, the most damaged limb first.")
	public ValidatedDouble firstAidSuppliesLimbHealthRegenerated = FzzyConfigBridge.field("body_damage", "firstAidSuppliesLimbHealthRegenerated");

	@Comment("How a player's limb health regenerated is defined. Accepted values are as follows: SIMPLE - The limb health regenerated is a fixed value defined in First Aid Supplies Limb Health Regenerated. PLAYER_DYNAMIC - The limb health regenerated is a percentage value of the player max health using the percentage value defined in First Aid Supplies Limb Health Regenerated. LIMB_DYNAMIC - The limb health regenerated is a percentage value of the limb max health using the percentage value defined in First Aid Supplies Limb Health Regenerated. Any other value will default to SIMPLE.")
	public ValidatedEnum<EnumUtil.limbRegenerationMode> firstAidSuppliesLimbRegenerationMode = FzzyConfigBridge.field("body_damage", "firstAidSuppliesLimbRegenerationMode");

	@Comment("Whether the exceeded limb health regenerated will heal the next most damaged limb. Only available for Regeneration Mode SIMPLE or PLAYER_DYNAMIC.")
	public ValidatedBoolean firstAidSuppliesHealingOverflow = FzzyConfigBridge.field("body_damage", "firstAidSuppliesHealingOverflow");

	@Comment("How fast in ticks the First Aid Supplies will heal limbs. 20 ticks = 1s")
	public ValidatedInt firstAidSuppliesTickTimer = FzzyConfigBridge.field("body_damage", "firstAidSuppliesTickTimer");

	@Comment("Whether the First Aid Supplies exhaust food when healing limbs, such as the other healing items.")
	public ValidatedBoolean firstAidSuppliesExhaustsFood = FzzyConfigBridge.field("body_damage", "firstAidSuppliesExhaustsFood");

	@Comment("The First Aid Supplies will heal limbs faster when the player is under one of the mentioned effect.")
	public ValidatedList<String> firstAidSuppliesBoostedOnEffects = FzzyConfigBridge.field("body_damage", "firstAidSuppliesBoostedOnEffects");

	@Comment("How much the First Aid Supplies tick timer is multiplied when boosted. A value of 1 would deactivate the speed boost. 0.5 makes the heal twice faster.")
	@ConfigGroup.Pop
	public ValidatedDouble firstAidSuppliesBoostedTickTimerMultiplier = FzzyConfigBridge.field("body_damage", "firstAidSuppliesBoostedTickTimerMultiplier");

	public ConfigGroup group_healing_limbs_healing_items = new ConfigGroup("group_healing_limbs_healing_items");

	@Comment("Amount of player health regenerated by the Recovery Effect every effect tick. For comparison, the Regeneration Effect healing amount is 1.")
	public ValidatedDouble recoveryEffectHealingValue = FzzyConfigBridge.field("body_damage", "recoveryEffectHealingValue");

	@Comment("Item use time in ticks.")
	public ValidatedInt healingHerbsUseTime = FzzyConfigBridge.field("body_damage", "healingHerbsUseTime");

	@Comment("Item use time in ticks.")
	public ValidatedInt plasterUseTime = FzzyConfigBridge.field("body_damage", "plasterUseTime");

	@Comment("Item use time in ticks.")
	public ValidatedInt bandageUseTime = FzzyConfigBridge.field("body_damage", "bandageUseTime");

	@Comment("Item use time in ticks.")
	public ValidatedInt tonicUseTime = FzzyConfigBridge.field("body_damage", "tonicUseTime");

	@Comment("Item use time in ticks.")
	@ConfigGroup.Pop
	public ValidatedInt medkitUseTime = FzzyConfigBridge.field("body_damage", "medkitUseTime");

	public ConfigGroup group_healing_limbs_morphine = new ConfigGroup("group_healing_limbs_morphine");

	@Comment("Item use time in ticks.")
	public ValidatedInt morphineUseTime = FzzyConfigBridge.field("body_damage", "morphineUseTime");

	@Comment("Painkiller effect duration in ticks. This effect prevents the player to be affected by broken limbs effects.")
	@ConfigGroup.Pop
	public ValidatedInt morphinePainkillerTickDuration = FzzyConfigBridge.field("body_damage", "morphinePainkillerTickDuration");

	public ConfigGroup group_body_parts_health = new ConfigGroup("group_body_parts_health");

	@Comment("How a player's body part health is determined. Accepted values are as follows: SIMPLE - Body parts will have initial fixed values. The body parts health define the health value. In this case, if the 'headPartHeath = 10', the head will have '10' health. DYNAMIC - Body parts will have dynamic values based on the player's max health. In this case, the body parts health is a multiplier of the player's max health. In this case, if the 'headPartHeath = 0.3', the head will have '0.3' * 'player max health' health. Any other value will default to SIMPLE.")
	public ValidatedEnum<EnumUtil.bodyPartHealthMode> bodyPartHealthMode = FzzyConfigBridge.field("body_damage", "bodyPartHealthMode");

	public ValidatedDouble headPartHealth = FzzyConfigBridge.field("body_damage", "headPartHealth");

	@Comment("Both arms will have this health.")
	public ValidatedDouble armsPartHealth = FzzyConfigBridge.field("body_damage", "armsPartHealth");

	public ValidatedDouble chestPartHealth = FzzyConfigBridge.field("body_damage", "chestPartHealth");

	@Comment("Both legs will have this health.")
	public ValidatedDouble legsPartHealth = FzzyConfigBridge.field("body_damage", "legsPartHealth");

	@Comment("Both feet will have this health.")
	@ConfigGroup.Pop
	public ValidatedDouble feetPartHealth = FzzyConfigBridge.field("body_damage", "feetPartHealth");

	public ConfigGroup group_body_parts_effects_head = new ConfigGroup("group_body_parts_effects_head");

	@Comment("The list of effects that will be triggered when the head is damaged by the percentage of remaining head health defined in the thresholds.")
	public ValidatedList<String> headPartEffects = FzzyConfigBridge.field("body_damage", "headPartEffects");

	@Comment("The list of amplifiers the effect will have. 0 means the basic effect, 1 means the effect is amplified once.")
	public ValidatedList<Integer> headPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "headPartEffectAmplifiers");

	@Comment("The list of thresholds for which each effect will be triggered. A threshold is a percentage of remaining head health. 0 means the head is fully damaged.")
	@ConfigGroup.Pop
	public ValidatedList<Double> headPartEffectThresholds = FzzyConfigBridge.field("body_damage", "headPartEffectThresholds");

	public ConfigGroup group_body_parts_effects_arms = new ConfigGroup("group_body_parts_effects_arms");

	public ValidatedList<String> armsPartEffects = FzzyConfigBridge.field("body_damage", "armsPartEffects");

	public ValidatedList<Integer> armsPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "armsPartEffectAmplifiers");

	public ValidatedList<Double> armsPartEffectThresholds = FzzyConfigBridge.field("body_damage", "armsPartEffectThresholds");

	@Comment("These effects will be triggered when both arms reach the thresholds. If a same effect is used with a higher amplifier, the higher prevails (normal Minecraft behaviour).")
	public ValidatedList<String> bothArmsPartEffects = FzzyConfigBridge.field("body_damage", "bothArmsPartEffects");

	public ValidatedList<Integer> bothArmsPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "bothArmsPartEffectAmplifiers");

	@ConfigGroup.Pop
	public ValidatedList<Double> bothArmsPartEffectThresholds = FzzyConfigBridge.field("body_damage", "bothArmsPartEffectThresholds");

	public ConfigGroup group_body_parts_effects_chest = new ConfigGroup("group_body_parts_effects_chest");

	public ValidatedList<String> chestPartEffects = FzzyConfigBridge.field("body_damage", "chestPartEffects");

	public ValidatedList<Integer> chestPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "chestPartEffectAmplifiers");

	@ConfigGroup.Pop
	public ValidatedList<Double> chestPartEffectThresholds = FzzyConfigBridge.field("body_damage", "chestPartEffectThresholds");

	public ConfigGroup group_body_parts_effects_legs = new ConfigGroup("group_body_parts_effects_legs");

	public ValidatedList<String> legsPartEffects = FzzyConfigBridge.field("body_damage", "legsPartEffects");

	public ValidatedList<Integer> legsPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "legsPartEffectAmplifiers");

	public ValidatedList<Double> legsPartEffectThresholds = FzzyConfigBridge.field("body_damage", "legsPartEffectThresholds");

	public ValidatedList<String> bothLegsPartEffects = FzzyConfigBridge.field("body_damage", "bothLegsPartEffects");

	public ValidatedList<Integer> bothLegsPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "bothLegsPartEffectAmplifiers");

	@ConfigGroup.Pop
	public ValidatedList<Double> bothLegsPartEffectThresholds = FzzyConfigBridge.field("body_damage", "bothLegsPartEffectThresholds");

	public ConfigGroup group_body_parts_effects_feet = new ConfigGroup("group_body_parts_effects_feet");

	public ValidatedList<String> feetPartEffects = FzzyConfigBridge.field("body_damage", "feetPartEffects");

	public ValidatedList<Integer> feetPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "feetPartEffectAmplifiers");

	public ValidatedList<Double> feetPartEffectThresholds = FzzyConfigBridge.field("body_damage", "feetPartEffectThresholds");

	public ValidatedList<String> bothFeetPartEffects = FzzyConfigBridge.field("body_damage", "bothFeetPartEffects");

	public ValidatedList<Integer> bothFeetPartEffectAmplifiers = FzzyConfigBridge.field("body_damage", "bothFeetPartEffectAmplifiers");

	@ConfigGroup.Pop
	public ValidatedList<Double> bothFeetPartEffectThresholds = FzzyConfigBridge.field("body_damage", "bothFeetPartEffectThresholds");

	public ConfigGroup group_integration_meds_and_herbs = new ConfigGroup("group_integration_meds_and_herbs");

	@Comment("Painkiller Addiction effect prevents the abusive usage of Painkiller effect that prevents all negative effects from limbs. Syringe Morphine share the same configuration as Morphine item")
	@ConfigGroup.Pop
	public ValidatedBoolean morphineSyringeApplyPainkillerAddiction = FzzyConfigBridge.field("body_damage", "morphineSyringeApplyPainkillerAddiction");
}
