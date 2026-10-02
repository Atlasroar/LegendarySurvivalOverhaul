package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonAirQualityDimension;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class AirQualityDataManager {

    public static IAirQualityDimensionManager internalDimension;

    /**
     * Retrieves the data-driven air quality configuration for the provided dimension registry name.
     */
    public static JsonAirQualityDimension getDimension(ResourceLocation dimensionRegistryName) {
        return internalDimension.get(dimensionRegistryName);
    }

    /**
     * Retrieves ambient quality using opt-in vanilla-dimension overrides, then datapack profiles and the
     * configured unprofiled-dimension fallback (GREEN by default).
     * <p>
     * Unless explicitly overridden, the Nether's ambient YELLOW air is applied directly rather than relying solely on the
     * data-driven dimension profile JSON: unlike the block/item tags backing air providers (loaded through
     * vanilla's own robust tag reload pipeline), dimension profiles are loaded through this mod's own
     * {@link SimpleJsonResourceReloadListener}-based manager, and a load failure there (a missing/invalid
     * datapack entry, a reload ordering issue, etc.) would otherwise silently leave the whole Nether feeling
     * breathable with no indication anything is wrong.
     */
    public static AirQualityLevel getAirQualityAtLevelByDimension(Level level, int height) {
        if (Config.Baked.overrideVanillaDimensionProfiles) {
            if (level.dimension() == Level.OVERWORLD)
                return height >= Config.Baked.overworldMinY && height <= Config.Baked.overworldMaxY
                        ? Config.Baked.overworldAir : Config.Baked.overworldOutsideAir;
            if (level.dimension() == Level.NETHER) return Config.Baked.netherAir;
            if (level.dimension() == Level.END) return Config.Baked.endAir;
        }
        if (level.dimension() == Level.NETHER) return AirQualityLevel.YELLOW;

        JsonAirQualityDimension dimension = internalDimension.get(level.dimension().location());
        if (dimension == null) {
            return Config.Baked.unconfiguredDimensionAir;
        }
        return dimension.getAirQualityAtHeight(height);
    }
}
