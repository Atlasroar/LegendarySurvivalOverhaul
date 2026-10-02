package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public final class FabricConsumableHooks {
    private FabricConsumableHooks() {
    }

    public static void onFinishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player))
            return;

        if (!level.isClientSide) {
            var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            TemperatureUtil.applyConsumableTemperature(player, itemId);
            ThirstUtil.takeDrink(player, stack);

            applyEnchantedGoldenAppleBonus(stack, player);
        }

        if (!(stack.getItem() instanceof BodyHealingItem))
            BodyDamageUtil.applyConsumableHealing(player, stack, true);
    }

    /**
     * Enchanted Golden Apples grant a larger, item-specific Shield Health bonus than the
     * generic vanilla-Absorption override (see {@link FabricMobEffectHooks}) and also repair
     * (restore) Heart Containers, e.g. ones previously lost to death. Runs before vanilla's own
     * eat-effect application (this hook fires at the head of {@code ItemStack#finishUsingItem}),
     * so {@link FabricMobEffectHooks} is notified to skip its flat bonus for this player instead
     * of stacking on top of this one, while the vanilla Absorption effect is still cancelled as usual.
     */
    private static void applyEnchantedGoldenAppleBonus(ItemStack stack, Player player) {
        if (!Config.Baked.healthOverhaulEnabled
                || !Config.Baked.absorptionEffectOverride
                || !Config.Baked.enchantedGoldenAppleOverrideEnabled
                || !stack.is(Items.ENCHANTED_GOLDEN_APPLE))
            return;

        HealthCapability health = CapabilityUtil.getHealthCapability(player);
        health.addShieldHealth((float) Config.Baked.enchantedGoldenAppleShieldHealth);
        health.addAdditionalHealth((float) (Config.Baked.enchantedGoldenAppleHeartContainersRepaired * 2));
        HealthUtil.updatePlayerMaxHealthAttribute(player);

        FabricMobEffectHooks.markEnchantedGoldenAppleHandled(player.getUUID());
    }
}
