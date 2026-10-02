package sfiomn.legendarysurvivaloverhaul.common.integration.vanillabackport;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class VanillaBackportCompat {
    private static final boolean LOADED = FabricLoader.getInstance().isModLoaded("vanillabackport");
    private static final ResourceKey<Biome> SULFUR_CAVES = ResourceKey.create(
            Registries.BIOME, new ResourceLocation("minecraft", "sulfur_caves"));

    private VanillaBackportCompat() {}

    public static boolean hasSulfurAir(Level level, BlockPos eyePosition) {
        return LOADED && Config.Baked.airQualityEnabled && Config.Baked.sulfurCaveAirEnabled
                && level.getBiome(eyePosition).is(SULFUR_CAVES);
    }

}
