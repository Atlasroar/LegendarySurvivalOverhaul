package sfiomn.legendarysurvivaloverhaul.common.integration.meadow;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;


public class MeadowUtil {

    public static boolean isInWoodenWaterCauldron(BlockState blockState) {
        return LegendarySurvivalOverhaul.meadowLoaded && BuiltInRegistries.BLOCK.getKey(blockState.getBlock())
                .equals(new ResourceLocation("meadow", "wooden_water_cauldron"));
    }
}
