package lso.test;

import lso.check.WetnessLookupCheck;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;

@Mixin(WetnessCapability.class)
public abstract class WetnessParticleCheckMixin {
    @Inject(method = "worldParticles", at = @At("HEAD"), remap = false)
    private void test$countParticleCheck(Player player, Level level, CallbackInfo ci) {
        WetnessLookupCheck.recordParticleCheck();
    }
}
