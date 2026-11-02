package sfiomn.legendarysurvivaloverhaul.common.integration.weathering;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public final class WeatheringStorms {
    public enum Exposure { NONE, SNOW, SAND }

    private WeatheringStorms() {}

    public static Exposure exposure(Player player) {
        if (!enabled(player) || !player.level().isRaining())
            return Exposure.NONE;
        Exposure storm = WeatheringStormIntegration.detect(player.level(), player.blockPosition());
        if (storm == Exposure.NONE || sheltered(player.level(), BlockPos.containing(
                player.getX(), player.getEyeY(), player.getZ())))
            return Exposure.NONE;
        if (storm == Exposure.SNOW && (player.hasEffect(MobEffectRegistry.HOT_DRINk.get())
                || FrostbiteEffect.playerIsImmuneToFrost(player)))
            return Exposure.NONE;
        if (storm == Exposure.SAND && (player.hasEffect(MobEffectRegistry.COLD_DRINK.get())
                || HeatStrokeEffect.playerIsImmuneToHeat(player)))
            return Exposure.NONE;
        return storm;
    }

    public static boolean sheltered(Level level, BlockPos eyes) {
        BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                boolean covered = false;
                for (int y = 1; y <= 10; y++) {
                    probe.set(eyes.getX() + x, eyes.getY() + y, eyes.getZ() + z);
                    if (!level.hasChunkAt(probe))
                        return false;
                    if (level.getBlockState(probe).getLightBlock(level, probe) > 0) {
                        covered = true;
                        break;
                    }
                }
                if (!covered)
                    return false;
            }
        }
        return true;
    }

    public static void onFreezingWindHit(Player player) {
        if (enabled(player) && !FrostbiteEffect.playerIsImmuneToFrost(player))
            CapabilityUtil.getTempCapability(player).onFreezingWindHit(player);
    }

    private static boolean enabled(Player player) {
        return WeatheringCompatibility.AVAILABLE && Config.Baked.temperatureEnabled && Config.Baked.immersiveWeatheringStormsEnabled
                && !player.level().isClientSide && player.isAlive() && !player.isCreative() && !player.isSpectator();
    }
}
