package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricSleepHooks;

@Mixin(ServerLevel.class)
public abstract class ServerLevelSleepMixin {
    @Inject(method = "wakeUpAllPlayers", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$recoverPlayersAfterSleep(CallbackInfo callbackInfo) {
        FabricSleepHooks.onSleepFinished((ServerLevel) (Object) this);
    }
}
