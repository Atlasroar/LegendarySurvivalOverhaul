package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.CanteenEnchantment;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"))
    private static void legendarysurvivaloverhaul$restrictCanteenEnchantments(
            int level, ItemStack stack, boolean allowTreasure,
            CallbackInfoReturnable<List<EnchantmentInstance>> callback) {
        // Vanilla's level-based loot selection checks the category, not canEnchant.
        if (!stack.is(Items.BOOK))
            callback.getReturnValue().removeIf(entry ->
                    entry.enchantment instanceof CanteenEnchantment && !entry.enchantment.canEnchant(stack));
    }
}
