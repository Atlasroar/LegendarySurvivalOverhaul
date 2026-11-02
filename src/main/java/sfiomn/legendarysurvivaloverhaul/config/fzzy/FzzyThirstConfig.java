package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import sfiomn.legendarysurvivaloverhaul.config.*;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

/**
 * Thirst settings, optionally editable in-game through Fzzy Config (Mod Menu / {@code /configure}).
 * Generated from the former ForgeConfigSpec definition; field names are referenced by {@link Config.Baked}.
 */
@SuppressWarnings("unused")
public class FzzyThirstConfig extends me.fzzyhmstrs.fzzy_config.config.Config
{
	public FzzyThirstConfig()
	{
		super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "thirst"));
	}

	@Override
	public void onSyncClient()
	{
		FzzyConfigBridge.bake(this, "thirst");
	}

	@Override
	public void onSyncServer()
	{
		FzzyConfigBridge.bake(this, "thirst");
	}

	@Override
	public void onUpdateClient()
	{
		FzzyConfigBridge.bake(this, "thirst");
	}

	@Override
	public void onUpdateServer(ServerUpdateContext context)
	{
		FzzyConfigBridge.bake(this, "thirst");
	}

	@Comment("Whether the thirst system is enabled.")
	public ValidatedBoolean thirstEnabled = FzzyConfigBridge.field("thirst", "thirstEnabled");

	@Comment("If enabled, players will take damage from the complete dehydration.")
	public ValidatedBoolean dangerousDehydration = FzzyConfigBridge.field("thirst", "dangerousDehydration");

	@Comment("If enabled, each time the player receives a thirst effect, its duration will be added to the thirst effect duration if already on the player.")
	public ValidatedBoolean cumulativeThirstEffectDuration = FzzyConfigBridge.field("thirst", "cumulativeThirstEffectDuration");

	@Comment("Scaling of the damages dealt when completely dehydrated. Each tick damage will be increased by this value.")
	public ValidatedDouble dehydrationDamageScaling = FzzyConfigBridge.field("thirst", "dehydrationDamageScaling");

	@Comment("How much thirst exhaustion will be added every 50 ticks when the player suffers from non amplified Thirst Effect. The player will suffer Thirst Effect from dirty water for example.")
	public ValidatedDouble thirstEffectModifier = FzzyConfigBridge.field("thirst", "thirstEffectModifier");

	public ConfigGroup group_exhaustion = new ConfigGroup("group_exhaustion");

	@Comment("Hydration exhausted every 10 ticks.")
	public ValidatedDouble baseHydrationExhaustion = FzzyConfigBridge.field("thirst", "baseHydrationExhaustion");

	@Comment("Hydration exhausted when sprinting, replacing the base hydration exhausted.")
	public ValidatedDouble sprintingHydrationExhaustion = FzzyConfigBridge.field("thirst", "sprintingHydrationExhaustion");

	@Comment("Hydration exhausted on every jump.")
	public ValidatedDouble onJumpHydrationExhaustion = FzzyConfigBridge.field("thirst", "onJumpHydrationExhaustion");

	@Comment("Hydration exhausted on every block break.")
	public ValidatedDouble onBlockBreakHydrationExhaustion = FzzyConfigBridge.field("thirst", "onBlockBreakHydrationExhaustion");

	@Comment("Hydration exhausted on every attack.")
	@ConfigGroup.Pop
	public ValidatedDouble onAttackHydrationExhaustion = FzzyConfigBridge.field("thirst", "onAttackHydrationExhaustion");

	public ConfigGroup group_canteen = new ConfigGroup("group_canteen");

	@Comment("If enabled, the player can water himself by using the canteen while crouching. This will increase the player wetness and remove fire.")
	public ValidatedBoolean selfWateringCanteenEnabled = FzzyConfigBridge.field("thirst", "selfWateringCanteenEnabled");

	@Comment("If Self Watering Canteen and Wetness are enabled, defines how much wetness is added to the player. Set this value to 0 to have no wetness added. By default, the maximum wetness is 400.")
	public ValidatedInt selfWateringCanteenWetnessIncrease = FzzyConfigBridge.field("thirst", "selfWateringCanteenWetnessIncrease");

	@Comment("Capacity of the canteen used to store water.")
	public ValidatedInt canteenCapacity = FzzyConfigBridge.field("thirst", "canteenCapacity");

	@Comment("Capacity of the large canteen used to store water.")
	public ValidatedInt largeCanteenCapacity = FzzyConfigBridge.field("thirst", "largeCanteenCapacity");

	@Comment("Allow override of purified water stored in canteen with normal water.")
	@ConfigGroup.Pop
	public ValidatedBoolean allowOverridePurifiedWater = FzzyConfigBridge.field("thirst", "allowOverridePurifiedWater");

	public ConfigGroup group_nether_chalice = new ConfigGroup("group_nether_chalice");

	@Comment("Amount of hydration recovered when drinking from lava.")
	public ValidatedInt hydrationLava = FzzyConfigBridge.field("thirst", "hydrationLava");

	@Comment("Amount of saturation recovered when drinking from lava.")
	@ConfigGroup.Pop
	public ValidatedDouble saturationLava = FzzyConfigBridge.field("thirst", "saturationLava");

	public ConfigGroup group_juices = new ConfigGroup("group_juices");

	@Comment("Whether the player retrieves a glass bottle after drinking a juice.")
	@ConfigGroup.Pop
	public ValidatedBoolean glassBottleLootAfterDrink = FzzyConfigBridge.field("thirst", "glassBottleLootAfterDrink");

	public ConfigGroup group_vampirism = new ConfigGroup("group_vampirism");

	@Comment("If Vampirism is installed and if thirst enabled while being a vampire, keep the thirst system in addition to the vampiric one. If disabled, the thirst system will be disabled for vampires.")
	@ConfigGroup.Pop
	public ValidatedBoolean thirstEnabledIfVampire = FzzyConfigBridge.field("thirst", "thirstEnabledIfVampire");
}
