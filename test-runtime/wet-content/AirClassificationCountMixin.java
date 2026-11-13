package lso.test;

import lso.check.AirScanCheck;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;

@Mixin(AirQualityLevel.class)
public abstract class AirClassificationCountMixin {
    @Inject(method = "getAirQualityFromBlock", at = @At("HEAD"))
    private static void test$countClassification(BlockState state, CallbackInfoReturnable<AirQualityLevel> cir) {
        AirScanCheck.recordClassification();
    }
}
