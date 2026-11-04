package sfiomn.legendarysurvivaloverhaul.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.IceSkating;

// Adapted for LSO on 2026-11-02 from Frostiful's LivingEntityMovementMixin (LGPL-3.0), without Thermoo or CCA flags.
@Mixin(LivingEntity.class)
public abstract class LivingEntityIceSkatingMixin extends Entity {
    protected LivingEntityIceSkatingMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float legendarysurvivaloverhaul$skateFriction(float original) {
        BlockState floor = level().getBlockState(getBlockPosBelowThatAffectsMyMovement());
        return IceSkating.friction((LivingEntity) (Object) this, floor, original);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void legendarysurvivaloverhaul$skateTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        BlockState floor = level().getBlockState(getBlockPosBelowThatAffectsMyMovement());
        IceSkating.updateWalkingPenalty(entity, floor);
        if (level().isClientSide && onGround() && IceSkating.skating(entity, floor)
                && getDeltaMovement().horizontalDistanceSqr() >= 0.04 && tickCount % 5 == 0) {
            level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, floor),
                    getX(), getY(), getZ(), 0, 0.1, 0);
        }
    }

    @Inject(method = "push", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$skateLanding(Entity other, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        IceSkating.damageLanding(entity, other);
    }
}
