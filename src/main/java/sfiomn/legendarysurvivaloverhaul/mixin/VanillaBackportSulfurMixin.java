package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.integration.vanillabackport.VanillaBackportCompat;

@Pseudo
@Mixin(targets = "com.blackgear.vanillabackport.common.level.block_entities.PotentSulfurBlockEntity", remap = false)
public abstract class VanillaBackportSulfurMixin {
    @Inject(method = "applyNauseaEffect", at = @At("HEAD"), cancellable = true, remap = false)
    private static void legendarysurvivaloverhaul$protectFromSulfurGas(
            LivingEntity entity, CallbackInfo callback) {
        if (VanillaBackportCompat.blocksSulfurNausea(entity)) callback.cancel();
    }
}
