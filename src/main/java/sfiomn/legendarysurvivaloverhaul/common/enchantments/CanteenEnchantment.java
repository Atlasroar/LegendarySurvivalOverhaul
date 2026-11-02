package sfiomn.legendarysurvivaloverhaul.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public abstract class CanteenEnchantment extends Enchantment {
    protected CanteenEnchantment(Rarity rarity) {
        super(rarity, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{});
    }

    @Override
    public final boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof CanteenItem;
    }

    @Override
    public final boolean isTreasureOnly() {
        return true;
    }

    @Override
    public final boolean isTradeable() {
        return false;
    }
}
