package lso.test;

import lso.check.WetnessLookupCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class WetnessEnvironmentLookupMixin {
    @Inject(method = "getFluidState", at = @At("HEAD"))
    private void test$countFluid(BlockPos pos, CallbackInfoReturnable<FluidState> cir) {
        WetnessLookupCheck.recordFluid(pos);
    }

    @Inject(method = "getBlockState", at = @At("HEAD"))
    private void test$countBlock(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        WetnessLookupCheck.recordBlock(pos);
    }
}
