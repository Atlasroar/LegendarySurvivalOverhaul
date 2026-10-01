package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantmentRegistry;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Inject(method = "createResult", at = @At("TAIL"))
    private void legendarysurvivaloverhaul$purifyCanteenWithPurity(CallbackInfo callback) {
        ItemCombinerMenuAccessor menu = (ItemCombinerMenuAccessor) this;
        Container inputSlots = menu.legendarysurvivaloverhaul$getInputSlots();
        Container resultSlots = menu.legendarysurvivaloverhaul$getResultSlots();
        if (!(inputSlots.getItem(0).getItem() instanceof CanteenItem)) {
            return;
        }

        ItemStack result = resultSlots.getItem(0);
        if (!(result.getItem() instanceof CanteenItem)
                || EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.PURITY.get(), result) == 0) {
            return;
        }

        CanteenItem.onPurityApplied(result);
    }
}
