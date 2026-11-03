package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.IceSkating;

// Adapted for LSO on 2026-11-02 from Frostiful's LivingEntityMovementMixin (LGPL-3.0), without Thermoo or CCA flags.
@Mixin(LivingEntity.class)
public abstract class LivingEntityIceSkatingMixin extends Entity {
    protected LivingEntityIceSkatingMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float legendarysurvivaloverhaul$skateFriction(Block block) {
        return IceSkating.friction((LivingEntity) (Object) this, block.defaultBlockState(), block.getFriction());
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
        if (!level().isClientSide && IceSkating.wearingSkates(entity) && other instanceof LivingEntity target
                && getY() > target.getEyeY())
            target.hurt(level().damageSources().mobAttack(entity), 1.0F);
    }
}
