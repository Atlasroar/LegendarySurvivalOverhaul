package sfiomn.legendarysurvivaloverhaul.common.enchantments;

public class ReservoirEnchantment extends CanteenEnchantment {
    
    public ReservoirEnchantment() {
        super(Rarity.UNCOMMON);
    }
    
    @Override
    public int getMinCost(int level) {
        return 1 + level * 8;
    }
    
    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
}
