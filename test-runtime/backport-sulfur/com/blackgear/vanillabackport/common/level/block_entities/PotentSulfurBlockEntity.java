package com.blackgear.vanillabackport.common.level.block_entities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

// Minimal method-shape fixture, not an upstream block entity implementation.
public final class PotentSulfurBlockEntity {
    public static void expose(LivingEntity entity) {
        applyNauseaEffect(entity);
    }

    private static void applyNauseaEffect(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
    }
}
