package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;

public final class FabricMobEffectHooks {
    private FabricMobEffectHooks() {
    }

    public static boolean shouldCancelEffect(LivingEntity entity, MobEffectInstance effect) {
        if (!(entity instanceof Player player))
            return false;

        if (effect.getEffect() == MobEffects.ABSORPTION
                && Config.Baked.healthOverhaulEnabled
                && Config.Baked.absorptionEffectOverride) {
            if (!player.level().isClientSide) {
                HealthCapability health = CapabilityUtil.getHealthCapability(player);
                health.addShieldHealth(2);
            }
            return true;
        }

        return effect.getEffect() == MobEffectRegistry.THIRST.get()
                && TrinketsUtil.isTrinketItemEquipped(player, ItemRegistry.WATER_PURIFIER.get());
    }
}
