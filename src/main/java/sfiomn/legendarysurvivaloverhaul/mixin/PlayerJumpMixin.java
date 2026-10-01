package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricSurvivalCallbacks;

@Mixin(LivingEntity.class)
public abstract class PlayerJumpMixin {
    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$addJumpHydrationExhaustion(CallbackInfo callback) {
        if ((Object) this instanceof Player player)
            FabricSurvivalCallbacks.onPlayerJump(player);
    }
}
