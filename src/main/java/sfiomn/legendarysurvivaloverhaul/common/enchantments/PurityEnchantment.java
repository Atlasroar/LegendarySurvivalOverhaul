package sfiomn.legendarysurvivaloverhaul.common.enchantments;

public class PurityEnchantment extends CanteenEnchantment {
    
    public PurityEnchantment() {
        super(Rarity.RARE);
    }
    
    @Override
    public int getMinCost(int level) {
        return 20;
    }
    
    @Override
    public int getMaxCost(int level) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
}
