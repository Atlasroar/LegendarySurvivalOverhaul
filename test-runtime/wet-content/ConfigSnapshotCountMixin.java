package lso.test;

import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.config.ConfigNetworkingCheck;
import sfiomn.legendarysurvivaloverhaul.config.FileConfigStore;

import java.util.Map;

@Mixin(FileConfigStore.class)
public abstract class ConfigSnapshotCountMixin {
    @Inject(method = "serializeSnapshot", at = @At("HEAD"), remap = false)
    private static void test$countSnapshot(Map<String, Map<String, Object>> values,
                                           CallbackInfoReturnable<JsonObject> cir) {
        ConfigNetworkingCheck.recordSerialization();
    }
}
