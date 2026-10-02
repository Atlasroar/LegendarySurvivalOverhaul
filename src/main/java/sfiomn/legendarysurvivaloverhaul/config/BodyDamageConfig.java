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
 * Body Damage settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class BodyDamageConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public BodyDamageConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "body_damage"));
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

	@Comment("Whether body members receive localized damages. The damageSourceBodyParts.json allows you to define for specific damage source, the damage spread across specified body parts. The damage distribution can either be ONE_OF or ALL. ALL means the damage are equally divided across all body parts.")
	public ValidatedBoolean localizedBodyDamageEnabled = new ValidatedBoolean(true);

	@Comment("Multiply the damage taken by the player when shot in the head without helmet.")
	public ValidatedDouble headCriticalShotMultiplier = new ValidatedDouble(2.0d, 1000.0d, 1.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much of the hurt player's damage is assigned to the body parts.")
	public ValidatedDouble bodyDamageMultiplier = new ValidatedDouble(1.0d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much health ratio are recovered in all body parts from bed sleeping.")
	public ValidatedDouble bodyHealthRatioRecoveredFromSleep = new ValidatedDouble(1.0d, 1.0d, 0.0d, ValidatedNumber.WidgetType.SLIDER);

	@Comment("How much food is exhausted when a limb regenerates based on the amount of health regenerated. For each 1 health regenerated, the food is exhausted by this value.")
	public ValidatedDouble bodyHealingFoodExhaustion = new ValidatedDouble(0.5d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("The hunger bar won't drop below this value while body is healing. Each hunger icon has a value of 2 in the hunger bar.")
	public ValidatedInt minFoodOnBodyHealing = new ValidatedInt(0, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("How long in ticks is the Addiction Effect lasting. 0 deactivates the feature. The Addiction Effect prevents you from re-using the morphine item.")
	public ValidatedInt painkillerAddictionDuration = new ValidatedInt(3600, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_healing_limbs_passive_regeneration = new ConfigGroup("group_healing_limbs_passive_regeneration");

	@Comment("The limbs will be healed when the player is at max health.")
	public ValidatedBoolean passiveLimbRegenerationOnFullHealth = new ValidatedBoolean(true);

	@Comment("The limbs will be healed when the player is under one of the mentioned effect, the most damaged limb first.")
	public ValidatedList<String> passiveLimbRegenerationEffects = ValidatedList.ofString(List.of("minecraft:regeneration", "farmersdelight:comfort"));

	@Comment("Whether the amplification of the effect increase the limb regeneration speed. The speed increase follows the same logic as the Regeneration Effect Amplification : Amplifier 1 -> tick timer /2, Amplifier 2 -> tick timer /4, ...")
	public ValidatedBoolean passiveLimbRegenerationAmplificationEnabled = new ValidatedBoolean(true);

	@Comment("Amount of limb health regenerated, the most damaged limb first.")
	public ValidatedDouble passiveLimbHealthRegenerated = new ValidatedDouble(0.5, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How fast in ticks the limbs regenerate passively. 20 ticks = 1s")
	@ConfigGroup.Pop
	public ValidatedInt passiveLimbRegenerationTickTimer = new ValidatedInt(200, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_healing_limbs_proportional_regeneration = new ConfigGroup("group_healing_limbs_proportional_regeneration");

	@Comment("Minimum health threshold (as ratio of max health minus broken hearts) to start regenerating limbs.")
	public ValidatedDouble proportionalLimbRegenMinThreshold = new ValidatedDouble(0.5, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	@Comment("Minimum limb heal value per tick rate when at min threshold. Spread proportionally across all body parts.")
	public ValidatedDouble proportionalLimbRegenMinHealValue = new ValidatedDouble(0.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Maximum limb heal value per tick rate when at full health. Spread proportionally across all body parts.")
	public ValidatedDouble proportionalLimbRegenMaxHealValue = new ValidatedDouble(4.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How often in ticks the proportional regeneration occurs. 20 ticks = 1s")
	public ValidatedInt proportionalLimbRegenTickRate = new ValidatedInt(100, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Rate at which healing increases from min to max. 1.0 = linear, >1 = exponential.")
	@ConfigGroup.Pop
	public ValidatedDouble proportionalLimbRegenIncreaseRate = new ValidatedDouble(1.0, 10.0, 0.1, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_healing_limbs_first_aid_supplies = new ConfigGroup("group_healing_limbs_first_aid_supplies");

	@Comment("The First Aid Supplies regenerate limb health passively, either by holding it or equipping it as a trinket, the most damaged limb first.")
	public ValidatedDouble firstAidSuppliesLimbHealthRegenerated = new ValidatedDouble(0.25, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How a player's limb health regenerated is defined. Accepted values are as follows: SIMPLE - The limb health regenerated is a fixed value defined in First Aid Supplies Limb Health Regenerated. PLAYER_DYNAMIC - The limb health regenerated is a percentage value of the player max health using the percentage value defined in First Aid Supplies Limb Health Regenerated. LIMB_DYNAMIC - The limb health regenerated is a percentage value of the limb max health using the percentage value defined in First Aid Supplies Limb Health Regenerated. Any other value will default to SIMPLE.")
	public ValidatedEnum<EnumUtil.limbRegenerationMode> firstAidSuppliesLimbRegenerationMode = new ValidatedEnum<>(EnumUtil.limbRegenerationMode.LIMB_DYNAMIC);

	@Comment("Whether the exceeded limb health regenerated will heal the next most damaged limb. Only available for Regeneration Mode SIMPLE or PLAYER_DYNAMIC.")
	public ValidatedBoolean firstAidSuppliesHealingOverflow = new ValidatedBoolean(false);

	@Comment("How fast in ticks the First Aid Supplies will heal limbs. 20 ticks = 1s")
	public ValidatedInt firstAidSuppliesTickTimer = new ValidatedInt(300, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Whether the First Aid Supplies exhaust food when healing limbs, such as the other healing items.")
	public ValidatedBoolean firstAidSuppliesExhaustsFood = new ValidatedBoolean(true);

	@Comment("The First Aid Supplies will heal limbs faster when the player is under one of the mentioned effect.")
	public ValidatedList<String> firstAidSuppliesBoostedOnEffects = ValidatedList.ofString(List.of("minecraft:regeneration", "farmersdelight:comfort"));

	@Comment("How much the First Aid Supplies tick timer is multiplied when boosted. A value of 1 would deactivate the speed boost. 0.5 makes the heal twice faster.")
	@ConfigGroup.Pop
	public ValidatedDouble firstAidSuppliesBoostedTickTimerMultiplier = new ValidatedDouble(0.75, 1.0, 0.1, ValidatedNumber.WidgetType.SLIDER);

	public ConfigGroup group_healing_limbs_healing_items = new ConfigGroup("group_healing_limbs_healing_items");

	@Comment("Amount of player health regenerated by the Recovery Effect every effect tick. For comparison, the Regeneration Effect healing amount is 1.")
	public ValidatedDouble recoveryEffectHealingValue = new ValidatedDouble(0.1, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Item use time in ticks.")
	public ValidatedInt healingHerbsUseTime = new ValidatedInt(20, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Item use time in ticks.")
	public ValidatedInt plasterUseTime = new ValidatedInt(20, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Item use time in ticks.")
	public ValidatedInt bandageUseTime = new ValidatedInt(30, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Item use time in ticks.")
	public ValidatedInt tonicUseTime = new ValidatedInt(50, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Item use time in ticks.")
	@ConfigGroup.Pop
	public ValidatedInt medkitUseTime = new ValidatedInt(50, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_healing_limbs_morphine = new ConfigGroup("group_healing_limbs_morphine");

	@Comment("Item use time in ticks.")
	public ValidatedInt morphineUseTime = new ValidatedInt(30, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Painkiller effect duration in ticks. This effect prevents the player to be affected by broken limbs effects.")
	@ConfigGroup.Pop
	public ValidatedInt morphinePainkillerTickDuration = new ValidatedInt(1800, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ConfigGroup group_body_parts_health = new ConfigGroup("group_body_parts_health");

	@Comment("How a player's body part health is determined. Accepted values are as follows: SIMPLE - Body parts will have initial fixed values. The body parts health define the health value. In this case, if the 'headPartHeath = 10', the head will have '10' health. DYNAMIC - Body parts will have dynamic values based on the player's max health. In this case, the body parts health is a multiplier of the player's max health. In this case, if the 'headPartHeath = 0.3', the head will have '0.3' * 'player max health' health. Any other value will default to SIMPLE.")
	public ValidatedEnum<EnumUtil.bodyPartHealthMode> bodyPartHealthMode = new ValidatedEnum<>(EnumUtil.bodyPartHealthMode.DYNAMIC);

	public ValidatedDouble headPartHealth = new ValidatedDouble(0.4d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Both arms will have this health.")
	public ValidatedDouble armsPartHealth = new ValidatedDouble(0.4d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble chestPartHealth = new ValidatedDouble(0.6d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Both legs will have this health.")
	public ValidatedDouble legsPartHealth = new ValidatedDouble(0.6d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Both feet will have this health.")
	@ConfigGroup.Pop
	public ValidatedDouble feetPartHealth = new ValidatedDouble(0.4d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_body_parts_effects_head = new ConfigGroup("group_body_parts_effects_head");

	@Comment("The list of effects that will be triggered when the head is damaged by the percentage of remaining head health defined in the thresholds.")
	public ValidatedList<String> headPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":headache"));

	@Comment("The list of amplifiers the effect will have. 0 means the basic effect, 1 means the effect is amplified once.")
	public ValidatedList<Integer> headPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	@Comment("The list of thresholds for which each effect will be triggered. A threshold is a percentage of remaining head health. 0 means the head is fully damaged.")
	@ConfigGroup.Pop
	public ValidatedList<Double> headPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ConfigGroup group_body_parts_effects_arms = new ConfigGroup("group_body_parts_effects_arms");

	public ValidatedList<String> armsPartEffects = ValidatedList.ofString(List.of("minecraft:mining_fatigue"));

	public ValidatedList<Integer> armsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> armsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	@Comment("These effects will be triggered when both arms reach the thresholds. If a same effect is used with a higher amplifier, the higher prevails (normal Minecraft behaviour).")
	public ValidatedList<String> bothArmsPartEffects = ValidatedList.ofString(List.of("minecraft:weakness"));

	public ValidatedList<Integer> bothArmsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	@ConfigGroup.Pop
	public ValidatedList<Double> bothArmsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ConfigGroup group_body_parts_effects_chest = new ConfigGroup("group_body_parts_effects_chest");

	public ValidatedList<String> chestPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":vulnerability"));

	public ValidatedList<Integer> chestPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	@ConfigGroup.Pop
	public ValidatedList<Double> chestPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ConfigGroup group_body_parts_effects_legs = new ConfigGroup("group_body_parts_effects_legs");

	public ValidatedList<String> legsPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"));

	public ValidatedList<Integer> legsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> legsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> bothLegsPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"));

	public ValidatedList<Integer> bothLegsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(1));

	@ConfigGroup.Pop
	public ValidatedList<Double> bothLegsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ConfigGroup group_body_parts_effects_feet = new ConfigGroup("group_body_parts_effects_feet");

	public ValidatedList<String> feetPartEffects = ValidatedList.ofString(Collections.singletonList("minecraft:slowness"));

	public ValidatedList<Integer> feetPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(Collections.singletonList(0));

	public ValidatedList<Double> feetPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(Collections.singletonList(0.2));

	public ValidatedList<String> bothFeetPartEffects = ValidatedList.ofString(Collections.singletonList("minecraft:slowness"));

	public ValidatedList<Integer> bothFeetPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(Collections.singletonList(1));

	@ConfigGroup.Pop
	public ValidatedList<Double> bothFeetPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(Collections.singletonList(0.2));

	public ConfigGroup group_integration_meds_and_herbs = new ConfigGroup("group_integration_meds_and_herbs");

	@Comment("Painkiller Addiction effect prevents the abusive usage of Painkiller effect that prevents all negative effects from limbs. Syringe Morphine share the same configuration as Morphine item")
	@ConfigGroup.Pop
	public ValidatedBoolean morphineSyringeApplyPainkillerAddiction = new ValidatedBoolean(true);
}
