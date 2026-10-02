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

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FabricMobEffectHooks {
    private FabricMobEffectHooks() {
    }

    /**
     * Players for whom {@link FabricConsumableHooks} already granted a tailored Shield Health
     * bonus this item-use (e.g. eating an Enchanted Golden Apple). Consumed (removed) the next
     * time the generic vanilla Absorption override below would otherwise fire, so the flat
     * bonus doesn't stack on top of the item-specific one while the vanilla effect is still
     * cancelled as usual.
     */
    private static final Set<UUID> ENCHANTED_GOLDEN_APPLE_HANDLED = ConcurrentHashMap.newKeySet();

    public static void markEnchantedGoldenAppleHandled(UUID playerId) {
        ENCHANTED_GOLDEN_APPLE_HANDLED.add(playerId);
    }

    public static boolean shouldCancelEffect(LivingEntity entity, MobEffectInstance effect) {
        if (effect.getEffect() == MobEffects.CONFUSION
                && Config.Baked.respiratorBlocksSulfurNausea
                && TrinketsUtil.isRespiratorEquipped(entity))
            return true;

        if (!(entity instanceof Player player))
            return false;

        if (effect.getEffect() == MobEffects.ABSORPTION
                && Config.Baked.healthOverhaulEnabled
                && Config.Baked.absorptionEffectOverride) {
            if (!player.level().isClientSide && !ENCHANTED_GOLDEN_APPLE_HANDLED.remove(player.getUUID())) {
                HealthCapability health = CapabilityUtil.getHealthCapability(player);
                health.addShieldHealth(2);
            }
            return true;
        }

        return effect.getEffect() == MobEffectRegistry.THIRST.get()
                && TrinketsUtil.isTrinketItemEquipped(player, ItemRegistry.WATER_PURIFIER.get());
    }
}
