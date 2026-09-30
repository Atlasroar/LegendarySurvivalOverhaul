package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import sfiomn.legendarysurvivaloverhaul.common.level.feature.DoubleBlockFeature;
import sfiomn.legendarysurvivaloverhaul.common.level.feature.GoldFernFeature;

public class FeatureRegistry {
    public static final FabricDeferredRegister<Feature<?>> FEATURES = FabricDeferredRegister.create(BuiltInRegistries.FEATURE);

    public static final RegistryObject<DoubleBlockFeature> DOUBLE_BLOCK = FEATURES.register("double_block", () -> new DoubleBlockFeature(SimpleBlockConfiguration.CODEC));
    public static final RegistryObject<GoldFernFeature> GOLD_FERN = FEATURES.register("gold_fern", () -> new GoldFernFeature(SimpleBlockConfiguration.CODEC));

    public static void register(){
        FEATURES.registerAll();
    }
}
