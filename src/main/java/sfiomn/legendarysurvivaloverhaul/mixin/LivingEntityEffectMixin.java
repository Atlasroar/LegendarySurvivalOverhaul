package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricMobEffectHooks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityEffectMixin {
    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD"), cancellable = true)
    private void legendarysurvivaloverhaul$interceptEffects(
            MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> callback) {
        if (FabricMobEffectHooks.shouldCancelEffect((LivingEntity) (Object) this, effect))
            callback.setReturnValue(false);
    }
}
