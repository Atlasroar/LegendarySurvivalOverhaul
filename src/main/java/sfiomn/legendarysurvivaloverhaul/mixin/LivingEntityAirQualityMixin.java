package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.events.airquality.AirQualityHooks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityAirQualityMixin {
    @Unique
    private int legendarysurvivaloverhaul$originalAirSupply = Integer.MIN_VALUE;

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$captureAirSupply(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$originalAirSupply = ((LivingEntity) (Object) this).getAirSupply();
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void legendarysurvivaloverhaul$afterVanillaAirSupplyTick(CallbackInfo callbackInfo) {
        this.legendarysurvivaloverhaul$applyAirSupplyChange();
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAirSupply()I", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;decreaseAirSupply(I)I")))
    private void legendarysurvivaloverhaul$preserveDrowningCheck(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getAirSupply() <= 0) {
            this.legendarysurvivaloverhaul$applyAirSupplyChange();
        }
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
