package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.biome.Biomes;
import sfiomn.legendarysurvivaloverhaul.common.level.gen.ModPlacedFeatures;

public final class FabricWorldGenerationHooks {
    private FabricWorldGenerationHooks() {
    }

    public static void register() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.CRIMSON_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.CRIMSON_LILY_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.WARPED_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WARPED_LILY_PLACED_KEY);
        var overworld = BiomeSelectors.foundInOverworld();
        BiomeModifications.addFeature(overworld.and(context ->
                        context.getBiome().getBaseTemperature() <= 0.3F),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.ICE_FERN_PLACED_KEY);
        BiomeModifications.addFeature(overworld.and(context ->
                        context.getBiome().getBaseTemperature() >= 1.0F),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SUN_FERN_PLACED_KEY);
        BiomeModifications.addFeature(overworld.and(context ->
                        context.getBiome().getBaseTemperature() >= 1.0F),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WATER_PLANT_PLACED_KEY);
    }
}
