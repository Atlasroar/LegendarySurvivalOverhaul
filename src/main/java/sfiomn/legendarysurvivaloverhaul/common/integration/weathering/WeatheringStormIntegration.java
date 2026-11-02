package sfiomn.legendarysurvivaloverhaul.common.integration.weathering;

import com.ordana.immersive_weathering.sandstorm.Sandstorms;
import com.ordana.immersive_weathering.snowstorm.Snowstorms;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.common.integration.weathering.WeatheringStorms.Exposure;

final class WeatheringStormIntegration {
    private WeatheringStormIntegration() {}

    static Exposure detect(Level level, BlockPos pos) {
        if (Snowstorms.visualStorm(level, pos))
            return Exposure.SNOW;
        if (Sandstorms.ownsStorms() && Sandstorms.visualStorm(level, pos))
            return Exposure.SAND;
        return Exposure.NONE;
    }
}
