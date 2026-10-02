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
     * <p>
     * The Nether's ambient YELLOW air is always applied directly in code rather than relying solely on the
     * data-driven dimension profile JSON: unlike the block/item tags backing air providers (loaded through
     * vanilla's own robust tag reload pipeline), dimension profiles are loaded through this mod's own
     * {@link SimpleJsonResourceReloadListener}-based manager, and a load failure there (a missing/invalid
     * datapack entry, a reload ordering issue, etc.) would otherwise silently leave the whole Nether feeling
     * breathable with no indication anything is wrong.
     */
    public static AirQualityLevel getAirQualityAtLevelByDimension(Level level, int height) {
        if (level.dimension() == Level.NETHER) return AirQualityLevel.YELLOW;

        JsonAirQualityDimension dimension = internalDimension.get(level.dimension().location());
        if (dimension == null) {
            return AirQualityLevel.GREEN;
        }
        return dimension.getAirQualityAtHeight(height);
    }
}
