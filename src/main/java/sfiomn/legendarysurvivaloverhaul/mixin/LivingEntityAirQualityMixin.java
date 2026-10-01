package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.events.airquality.AirQualityHooks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAirQualityMixin {
    @Unique
    private int legendarysurvivaloverhaul$originalAirSupply = Integer.MIN_VALUE;

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z", shift = At.Shift.BEFORE))
    private void legendarysurvivaloverhaul$captureAirSupply(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$originalAirSupply = ((LivingEntity) (Object) this).getAirSupply();
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAirSupply(I)V", ordinal = 0, shift = At.Shift.AFTER), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;decreaseAirSupply(I)I")))
    private void legendarysurvivaloverhaul$afterAirDecrease(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$applyAirSupplyChange();
    }

    @ModifyVariable(method = "baseTick", at = @At("LOAD"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;")))
    private boolean legendarysurvivaloverhaul$afterAirLossCheck(boolean canLoseAir) {
        if (!canLoseAir) {
            this.legendarysurvivaloverhaul$applyAirSupplyChange();
        }
        return canLoseAir;
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAirSupply(I)V", ordinal = 0, shift = At.Shift.AFTER), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;increaseAirSupply(I)I")))
    private void legendarysurvivaloverhaul$afterAirIncrease(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$applyAirSupplyChange();
    }

    @Inject(method = "baseTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 0, shift = At.Shift.BEFORE), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;increaseAirSupply(I)I")))
    private void legendarysurvivaloverhaul$afterAirIncreaseFallback(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$applyAirSupplyChange();
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAirSupply()I", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;decreaseAirSupply(I)I")))
    private void legendarysurvivaloverhaul$preserveDrowningCheck(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getAirSupply() <= 0) {
            this.legendarysurvivaloverhaul$applyAirSupplyChange();
        }
    }

    @Inject(method = "baseTick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", ordinal = 0, shift = At.Shift.BEFORE), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSources;drown()Lnet/minecraft/world/damagesource/DamageSource;")))
    private void legendarysurvivaloverhaul$resetAirSupply(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$originalAirSupply = Integer.MIN_VALUE;
    }

    @Unique
    private void legendarysurvivaloverhaul$applyAirSupplyChange() {
        int originalAirSupply = this.legendarysurvivaloverhaul$originalAirSupply;
        if (originalAirSupply != Integer.MIN_VALUE) {
            AirQualityHooks.onAirSupplyTick((LivingEntity) (Object) this, originalAirSupply);
            this.legendarysurvivaloverhaul$originalAirSupply = Integer.MIN_VALUE;
        }
    }
}
