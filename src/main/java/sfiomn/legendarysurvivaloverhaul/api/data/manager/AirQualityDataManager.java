package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonAirQualityDimension;
import net.minecraft.world.level.Level;

public class AirQualityDataManager {

    public static IAirQualityDimensionManager internalDimension;

    /**
     * Retrieves the data-driven air quality configuration for the provided dimension registry name.
     */
    public static JsonAirQualityDimension getDimension(ResourceLocation dimensionRegistryName) {
        return internalDimension.get(dimensionRegistryName);
    }

    /**
     * Retrieves the air quality at the given height within the provided level's dimension, falling back to
     * breathable, green air for dimensions with no configured entry.
     */
    public static AirQualityLevel getAirQualityAtLevelByDimension(Level level, int height) {
        JsonAirQualityDimension dimension = internalDimension.get(level.dimension().location());
        if (dimension == null) {
            if (level.dimension() == Level.NETHER) return AirQualityLevel.YELLOW;
            return AirQualityLevel.GREEN;
        }
        return dimension.getAirQualityAtHeight(height);
    }
}
