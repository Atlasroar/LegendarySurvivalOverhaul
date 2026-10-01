package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.TooltipHandler;

import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {
    @Inject(method = "getTooltipImage", at = @At("RETURN"), cancellable = true)
    private void legendarysurvivaloverhaul$addHydrationTooltip(
            CallbackInfoReturnable<Optional<TooltipComponent>> callback) {
        if (callback.getReturnValue().isPresent())
            return;

        TooltipComponent component = TooltipHandler.getHydrationTooltipComponent((ItemStack) (Object) this);
        if (component != null)
            callback.setReturnValue(Optional.of(component));
    }
}
