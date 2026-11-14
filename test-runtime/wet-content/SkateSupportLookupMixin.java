package lso.test;

import lso.check.SkateLookupCheck;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class SkateSupportLookupMixin {
    @Inject(method = "getBlockPosBelowThatAffectsMyMovement", at = @At("HEAD"))
    private void test$countSupportLookup(CallbackInfoReturnable<BlockPos> cir) {
        SkateLookupCheck.record((Entity) (Object) this);
    }
}
