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
import java.util.Collections;
import java.util.List;

/**
 * Thirst settings, editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class ThirstConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public ThirstConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst"));
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

	@Comment("Whether the thirst system is enabled.")
	public ValidatedBoolean thirstEnabled = new ValidatedBoolean(true);

	@Comment("If enabled, players will take damage from the complete dehydration.")
	public ValidatedBoolean dangerousDehydration = new ValidatedBoolean(true);

	@Comment("If enabled, each time the player receives a thirst effect, its duration will be added to the thirst effect duration if already on the player.")
	public ValidatedBoolean cumulativeThirstEffectDuration = new ValidatedBoolean(true);

	@Comment("Scaling of the damages dealt when completely dehydrated. Each tick damage will be increased by this value.")
	public ValidatedDouble dehydrationDamageScaling = new ValidatedDouble(0.3d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("How much thirst exhaustion will be added every 50 ticks when the player suffers from non amplified Thirst Effect. The player will suffer Thirst Effect from dirty water for example.")
	public ValidatedDouble thirstEffectModifier = new ValidatedDouble(0.25d, 1000.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_exhaustion = new ConfigGroup("group_exhaustion");

	@Comment("Hydration exhausted every 10 ticks.")
	public ValidatedDouble baseHydrationExhaustion = new ValidatedDouble(0.03d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Hydration exhausted when sprinting, replacing the base hydration exhausted.")
	public ValidatedDouble sprintingHydrationExhaustion = new ValidatedDouble(0.1d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Hydration exhausted on every jump.")
	public ValidatedDouble onJumpHydrationExhaustion = new ValidatedDouble(0.15d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Hydration exhausted on every block break.")
	public ValidatedDouble onBlockBreakHydrationExhaustion = new ValidatedDouble(0.07d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	@Comment("Hydration exhausted on every attack.")
	@ConfigGroup.Pop
	public ValidatedDouble onAttackHydrationExhaustion = new ValidatedDouble(0.3d, 1000.0d, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_canteen = new ConfigGroup("group_canteen");

	@Comment("If enabled, the player can water himself by using the canteen while crouching. This will increase the player wetness and remove fire.")
	public ValidatedBoolean selfWateringCanteenEnabled = new ValidatedBoolean(true);

	@Comment("If Self Watering Canteen and Wetness are enabled, defines how much wetness is added to the player. Set this value to 0 to have no wetness added. By default, the maximum wetness is 400.")
	public ValidatedInt selfWateringCanteenWetnessIncrease = new ValidatedInt(400, 10000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Capacity of the canteen used to store water.")
	public ValidatedInt canteenCapacity = new ValidatedInt(10, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Capacity of the large canteen used to store water.")
	public ValidatedInt largeCanteenCapacity = new ValidatedInt(20, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS);

	@Comment("Allow override of purified water stored in canteen with normal water.")
	@ConfigGroup.Pop
	public ValidatedBoolean allowOverridePurifiedWater = new ValidatedBoolean(true);

	public ConfigGroup group_nether_chalice = new ConfigGroup("group_nether_chalice");

	@Comment("Amount of hydration recovered when drinking from lava.")
	public ValidatedInt hydrationLava = new ValidatedInt(6, 20, 0, ValidatedNumber.WidgetType.SLIDER);

	@Comment("Amount of saturation recovered when drinking from lava.")
	@ConfigGroup.Pop
	public ValidatedDouble saturationLava = new ValidatedDouble(4.0, 20.0, 0.0, ValidatedNumber.WidgetType.TEXTBOX);

	public ConfigGroup group_juices = new ConfigGroup("group_juices");

	@Comment("Whether the player retrieves a glass bottle after drinking a juice.")
	@ConfigGroup.Pop
	public ValidatedBoolean glassBottleLootAfterDrink = new ValidatedBoolean(true);

	public ConfigGroup group_vampirism = new ConfigGroup("group_vampirism");

	@Comment("If Vampirism is installed and if thirst enabled while being a vampire, keep the thirst system in addition to the vampiric one. If disabled, the thirst system will be disabled for vampires.")
	@ConfigGroup.Pop
	public ValidatedBoolean thirstEnabledIfVampire = new ValidatedBoolean(false);
}
