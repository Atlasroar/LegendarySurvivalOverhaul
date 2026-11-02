package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedEnum;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedDouble;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedInt;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedNumber;
import sfiomn.legendarysurvivaloverhaul.util.EnumUtil;

/**
 * Health settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class HealthConfig extends ConfigData
{
	public HealthConfig()
	{
		super("health");
	}

	public ValidatedBoolean healthOverhaulEnabled = new ValidatedBoolean(true);

	public ValidatedDouble initialHealth = new ValidatedDouble(20.0, 10000.0, 1.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble maxAdditionalHealth = new ValidatedDouble(20.0, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble healthRatioRecoveredFromSleep = new ValidatedDouble(1.0d, 1.0d, 0.0d, ValidatedNumber.WidgetType.SLIDER);

	public ValidatedBoolean naturalRegenerationEnabled = new ValidatedBoolean(false);

	public ValidatedBoolean customHealthRegenEnabled = new ValidatedBoolean(true);

	public ValidatedDouble customHealthRegenRate = new ValidatedDouble(1.0, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt customHealthRegenTickRate = new ValidatedInt(200, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble customHealthRegenFoodExhaustion = new ValidatedDouble(6.0, 100.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedDouble maxShieldHealth = new ValidatedDouble(20.0, 10000.0, 1.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedBoolean absorptionEffectOverride = new ValidatedBoolean(true);

	public ValidatedBoolean enchantedGoldenAppleOverrideEnabled = new ValidatedBoolean(true);

	public ValidatedDouble enchantedGoldenAppleShieldHealth = new ValidatedDouble(4.0, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedInt enchantedGoldenAppleHeartContainersRepaired = new ValidatedInt(1, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt heartsLostOnDeath = new ValidatedInt(1, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt permanentHearts = new ValidatedInt(1, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedInt resilientHeartsWithBrokenHearts = new ValidatedInt(2, 10000, 1, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	public ValidatedDouble brokenHeartsPerInjuredLimb = new ValidatedDouble(0.1, 10000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ValidatedEnum<EnumUtil.brokenHeartsPerInjuredLimbMode> brokenHeartsPerInjuredLimbMode = new ValidatedEnum<>(EnumUtil.brokenHeartsPerInjuredLimbMode.PLAYER_DYNAMIC);
}
