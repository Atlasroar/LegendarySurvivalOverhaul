package lso.test;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.config.ConfigNetworkingCheck;

@Mixin(targets = "sfiomn.legendarysurvivaloverhaul.config.ConfigNetworking")
public abstract class ConfigSyncSendMixin {
    @Inject(method = "send", at = @At("HEAD"), cancellable = true, remap = false)
    private static void test$captureSend(ServerPlayer player, String snapshot, CallbackInfo ci) {
        if (ConfigNetworkingCheck.captureSend(player, snapshot))
            ci.cancel();
    }
}
