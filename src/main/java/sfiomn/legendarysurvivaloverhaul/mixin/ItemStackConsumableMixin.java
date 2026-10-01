package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricConsumableHooks;

@Mixin(ItemStack.class)
public abstract class ItemStackConsumableMixin {
    @Shadow
    public abstract boolean isEmpty();

    @Inject(method = "finishUsingItem", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$applyConsumableEffects(
            Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> callback) {
        if (!isEmpty())
            FabricConsumableHooks.onFinishUsingItem((ItemStack) (Object) this, level, entity);
    }
}
