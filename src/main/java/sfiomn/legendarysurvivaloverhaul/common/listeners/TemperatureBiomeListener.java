package sfiomn.legendarysurvivaloverhaul.common.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ITemperatureBiomeManager;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureBiomesPacket;

import java.util.HashMap;
import java.util.Map;

public class TemperatureBiomeListener extends SimpleJsonResourceReloadListener implements ITemperatureBiomeManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<ResourceLocation, JsonTemperatureBiomeOverride> TEMPERATURE_BIOMES = new HashMap<>();

    public TemperatureBiomeListener() {
        super(GSON, LegendarySurvivalOverhaul.MOD_ID + "/temperature/biomes");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        TEMPERATURE_BIOMES.clear();

        resourceLocationJsonElementMap.forEach((key, json) -> {
            // Datapacks can register biome namespaces without a corresponding Fabric mod ID.
            JsonTemperatureBiomeOverride.CODEC.parse(JsonOps.INSTANCE, json)
                    .resultOrPartial(error -> LegendarySurvivalOverhaul.LOGGER.error(
                            "Failed parsing temperature biome {}: {}", key, error))
                    .ifPresent(temperatures -> TEMPERATURE_BIOMES.put(key, temperatures));
        });

        LegendarySurvivalOverhaul.LOGGER.info("Loaded {} temperature biomes", TEMPERATURE_BIOMES.size());
    }

    public static void sendDataToClient(net.minecraft.server.level.ServerPlayer player) {
        SyncTemperatureBiomesPacket.sendTo(player, TEMPERATURE_BIOMES);
    }

    public static void acceptServerTemperatureBiomes(Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes) {
        TEMPERATURE_BIOMES.clear();
        TEMPERATURE_BIOMES.putAll(temperatureBiomes);
    }

    @Override
    public JsonTemperatureBiomeOverride get(ResourceLocation itemRegistryName) {
        return TEMPERATURE_BIOMES.get(itemRegistryName);
    }
}