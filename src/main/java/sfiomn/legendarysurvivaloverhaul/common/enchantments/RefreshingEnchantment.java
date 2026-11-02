package sfiomn.legendarysurvivaloverhaul.common.enchantments;

public class RefreshingEnchantment extends CanteenEnchantment {
    
    public RefreshingEnchantment() {
        super(Rarity.UNCOMMON);
    }
    
    @Override
    public int getMinCost(int level) {
        return 10 + (level - 1) * 10;
    }
    
    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
}
