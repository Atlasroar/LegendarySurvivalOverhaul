package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonAirQualityDimension;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.IAirQualityDimensionManager;

import java.util.HashMap;
import java.util.Map;

public class AirQualityDimensionListener extends SimpleJsonResourceReloadListener implements IAirQualityDimensionManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonAirQualityDimension> AIR_QUALITY_DIMENSIONS = new HashMap<>();

    public AirQualityDimensionListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/air_quality/dimensions");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        AIR_QUALITY_DIMENSIONS.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                var parsedJson = JsonAirQualityDimension.CODEC.parse(JsonOps.INSTANCE, json);
                JsonAirQualityDimension airQualityDimension = parsedJson.getOrThrow(false, error -> LegendarySurvivalOverhaul.LOGGER.error("Failed parsing air quality dimension : {}", error));
                AIR_QUALITY_DIMENSIONS.put(key, airQualityDimension);
            } catch (JsonParseException error) {
                LegendarySurvivalOverhaul.LOGGER.error("Failed to parse air quality dimension json {}", key);
            }
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} air quality dimensions: {}", AIR_QUALITY_DIMENSIONS.size(), AIR_QUALITY_DIMENSIONS.keySet());
    }

    @Override
    public JsonAirQualityDimension get(ResourceLocation dimensionRegistryName) {
        return AIR_QUALITY_DIMENSIONS.get(dimensionRegistryName);
    }
}
