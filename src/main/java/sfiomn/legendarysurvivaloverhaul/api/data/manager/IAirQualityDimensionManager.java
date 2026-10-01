package sfiomn.legendarysurvivaloverhaul.api.data.manager;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonAirQualityDimension;

public interface IAirQualityDimensionManager {
    JsonAirQualityDimension get(ResourceLocation dimensionRegistryName);
}
