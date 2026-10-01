package sfiomn.legendarysurvivaloverhaul.common.integration.beachparty;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import org.jetbrains.annotations.NotNull;

public class BeachpartyUtil {

    public static boolean isUnderParasol(Level level, Player player, BlockPos pos) {
        if (LegendarySurvivalOverhaul.beachpartyLoaded) {

            if (player != null && player.getVehicle() != null
                    && player.getVehicle().getClass().getName().contains("ChairEntity")) {
                BlockPos checkPos = player.blockPosition().offset(0, 1, 0);
                return BuiltInRegistries.BLOCK.getKey(level.getBlockState(checkPos).getBlock())
                        .equals(new ResourceLocation("beachparty", "hooded_beach_chair"));
            } else {
                BlockPos checkPos = pos.offset(0, 1, 0);
                BlockState blockState = level.getBlockState(checkPos);
                if (BuiltInRegistries.BLOCK.getKey(blockState.getBlock())
                        .equals(new ResourceLocation("beachparty", "beach_parasol"))) {
                    return blockState.getProperties().stream()
                            .filter(property -> property.getName().equals("open"))
                            .anyMatch(property -> Boolean.parseBoolean(blockState.getValues().get(property).toString()));
                }
            }
        }
        return false;
    }

    public static boolean canProvideShade(@NotNull ResourceLocation itemRegistryName) {
        return LegendarySurvivalOverhaul.beachpartyLoaded && (itemRegistryName.toString().equals("beachparty:hooded_beach_chair") || itemRegistryName.toString().equals("beachparty:beach_parasol"));
    }
}
