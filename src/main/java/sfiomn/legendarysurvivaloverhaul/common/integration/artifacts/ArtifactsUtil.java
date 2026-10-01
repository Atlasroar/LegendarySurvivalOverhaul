package sfiomn.legendarysurvivaloverhaul.common.integration.artifacts;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import org.jetbrains.annotations.NotNull;

public class ArtifactsUtil {

    public static boolean isHoldingUmbrella(Player player) {
        if (LegendarySurvivalOverhaul.artifactsLoaded && player != null) {
            //  Check player is holding umbrella but not using it
            if (player.isHolding(itemStack -> BuiltInRegistries.ITEM.getKey(itemStack.getItem())
                    .equals(new ResourceLocation("artifacts", "umbrella"))))
                return !BuiltInRegistries.ITEM.getKey(player.getUseItem().getItem())
                        .equals(new ResourceLocation("artifacts", "umbrella"));
        }
        return false;
    }

    public static boolean canProvideShade(@NotNull ResourceLocation itemRegistryName) {
        return LegendarySurvivalOverhaul.artifactsLoaded && (itemRegistryName.toString().equals("artifacts:umbrella"));
    }
}
