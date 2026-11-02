package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedList;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedEnum;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;
import java.util.Collections;
import java.util.List;

/**
 * Body Damage settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class BodyDamageConfig extends ConfigData
{
	public BodyDamageConfig()
	{
		super("body_damage");
	}

	public ValidatedBoolean localizedBodyDamageEnabled = new ValidatedBoolean(true);

	public ValidatedDouble headCriticalShotMultiplier = new ValidatedDouble(2.0d, 1000.0d, 1.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble bodyDamageMultiplier = new ValidatedDouble(1.0d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble bodyHealthRatioRecoveredFromSleep = new ValidatedDouble(1.0d, 1.0d, 0.0d, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble bodyHealingFoodExhaustion = new ValidatedDouble(0.5d, 1000.0D, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt minFoodOnBodyHealing = new ValidatedInt(0, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt painkillerAddictionDuration = new ValidatedInt(3600, 100000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean passiveLimbRegenerationOnFullHealth = new ValidatedBoolean(true);

	public ValidatedList<String> passiveLimbRegenerationEffects = ValidatedList.ofString(List.of("minecraft:regeneration", "farmersdelight:comfort"));

	public ValidatedBoolean passiveLimbRegenerationAmplificationEnabled = new ValidatedBoolean(true);

	public ValidatedDouble passiveLimbHealthRegenerated = new ValidatedDouble(0.5, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt passiveLimbRegenerationTickTimer = new ValidatedInt(200, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble proportionalLimbRegenMinThreshold = new ValidatedDouble(0.5, 1.0, 0.0, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble proportionalLimbRegenMinHealValue = new ValidatedDouble(0.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble proportionalLimbRegenMaxHealValue = new ValidatedDouble(4.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt proportionalLimbRegenTickRate = new ValidatedInt(100, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble proportionalLimbRegenIncreaseRate = new ValidatedDouble(1.0, 10.0, 0.1, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble firstAidSuppliesLimbHealthRegenerated = new ValidatedDouble(0.25, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedEnum<EnumUtil.limbRegenerationMode> firstAidSuppliesLimbRegenerationMode = new ValidatedEnum<>(EnumUtil.limbRegenerationMode.LIMB_DYNAMIC);

	public ValidatedBoolean firstAidSuppliesHealingOverflow = new ValidatedBoolean(false);

	public ValidatedInt firstAidSuppliesTickTimer = new ValidatedInt(300, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedBoolean firstAidSuppliesExhaustsFood = new ValidatedBoolean(true);

	public ValidatedList<String> firstAidSuppliesBoostedOnEffects = ValidatedList.ofString(List.of("minecraft:regeneration", "farmersdelight:comfort"));

	public ValidatedDouble firstAidSuppliesBoostedTickTimerMultiplier = new ValidatedDouble(0.75, 1.0, 0.1, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedDouble recoveryEffectHealingValue = new ValidatedDouble(0.1, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt healingHerbsUseTime = new ValidatedInt(20, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt plasterUseTime = new ValidatedInt(20, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt bandageUseTime = new ValidatedInt(30, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt tonicUseTime = new ValidatedInt(50, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt medkitUseTime = new ValidatedInt(50, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt morphineUseTime = new ValidatedInt(30, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt morphinePainkillerTickDuration = new ValidatedInt(1800, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedEnum<EnumUtil.bodyPartHealthMode> bodyPartHealthMode = new ValidatedEnum<>(EnumUtil.bodyPartHealthMode.DYNAMIC);

	public ValidatedDouble headPartHealth = new ValidatedDouble(0.4d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble armsPartHealth = new ValidatedDouble(0.4d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble chestPartHealth = new ValidatedDouble(0.6d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble legsPartHealth = new ValidatedDouble(0.6d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble feetPartHealth = new ValidatedDouble(0.4d, 1000.0d, 0.0d, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedList<String> headPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":headache"));

	public ValidatedList<Integer> headPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> headPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> armsPartEffects = ValidatedList.ofString(List.of("minecraft:mining_fatigue"));

	public ValidatedList<Integer> armsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> armsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> bothArmsPartEffects = ValidatedList.ofString(List.of("minecraft:weakness"));

	public ValidatedList<Integer> bothArmsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> bothArmsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> chestPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":vulnerability"));

	public ValidatedList<Integer> chestPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> chestPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> legsPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"));

	public ValidatedList<Integer> legsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(0));

	public ValidatedList<Double> legsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> bothLegsPartEffects = ValidatedList.ofString(List.of(LegendarySurvivalOverhaul.MOD_ID + ":hard_falling"));

	public ValidatedList<Integer> bothLegsPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(List.of(1));

	public ValidatedList<Double> bothLegsPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(List.of(0.2));

	public ValidatedList<String> feetPartEffects = ValidatedList.ofString(Collections.singletonList("minecraft:slowness"));

	public ValidatedList<Integer> feetPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(Collections.singletonList(0));

	public ValidatedList<Double> feetPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(Collections.singletonList(0.2));

	public ValidatedList<String> bothFeetPartEffects = ValidatedList.ofString(Collections.singletonList("minecraft:slowness"));

	public ValidatedList<Integer> bothFeetPartEffectAmplifiers = new ValidatedInt(0, Integer.MAX_VALUE, 0).toList(Collections.singletonList(1));

	public ValidatedList<Double> bothFeetPartEffectThresholds = new ValidatedDouble(0.0, 1.0, 0.0).toList(Collections.singletonList(0.2));

	public ValidatedBoolean morphineSyringeApplyPainkillerAddiction = new ValidatedBoolean(true);
}
