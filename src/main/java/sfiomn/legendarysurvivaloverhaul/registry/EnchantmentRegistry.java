package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.registries.BuiltInRegistries;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.PurityEnchantment;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.RefreshingEnchantment;
import sfiomn.legendarysurvivaloverhaul.common.enchantments.ReservoirEnchantment;

public class EnchantmentRegistry {
    
    public static final FabricDeferredRegister<Enchantment> ENCHANTMENTS = FabricDeferredRegister.create(BuiltInRegistries.ENCHANTMENT);
    
    public static final RegistryObject<Enchantment> REFRESHING = ENCHANTMENTS.register("refreshing", RefreshingEnchantment::new);
    public static final RegistryObject<Enchantment> PURITY = ENCHANTMENTS.register("purity", PurityEnchantment::new);
    public static final RegistryObject<Enchantment> RESERVOIR = ENCHANTMENTS.register("reservoir", ReservoirEnchantment::new);
    
    public static void register() {
        ENCHANTMENTS.registerAll();
    }
}
