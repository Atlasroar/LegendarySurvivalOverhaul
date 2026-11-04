package lso.test;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LivingEntity.class, priority = 1100)
public abstract class FrictionCompatMixin {
    @Redirect(method = "travel", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
    private float test$externalFriction(Block block) {
        return block.getFriction() * (Boolean.getBoolean("lso.test.externalFriction") ? 0.5F : 1.0F);
    }
}
