import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureBiomesPacket;

import java.util.Map;

public final class BiomeCompatibilityCheck implements ModInitializer {
    private static final ResourceLocation TERRALITH = new ResourceLocation("terralith", "desert_canyon");
    private static final ResourceLocation PLAINS = new ResourceLocation("minecraft", "plains");

    private static void check(boolean value, String description) {
        if (!value) throw new AssertionError(description);
    }

    private static final class ClimateProbe extends ModifierBase {
        float temperature(Level level, Biome biome) {
            return getNormalizedTempForBiome(level, biome);
        }

        float humidity(Level level, Biome biome) {
            return getHumidityForBiome(level, biome);
        }
    }

    private static final class ReloadProbe extends TemperatureBiomeListener {
        void reload(MinecraftServer server, Map<ResourceLocation, JsonElement> entries) {
            apply(entries, server.getResourceManager(), server.getProfiler());
        }
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                check(!FabricLoader.getInstance().isModLoaded("terralith"),
                        "Run fixture without Terralith: namespace must not require a mod ID");
                var fromPack = TemperatureDataManager.getBiome(TERRALITH);
                check(fromPack != null && fromPack.temperature == 1.75f && fromPack.isDry,
                        "Actual datapack override loads for absent-mod namespace");

                var level = server.overworld();
                var plains = server.registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
                var climate = new ClimateProbe();
                float nativeTemperature = climate.temperature(level, plains);
                check(Math.abs(nativeTemperature - (plains.getBaseTemperature() + 0.5f) / 2.5f) < 0.00001f,
                        "Native biome temperature retained without override");
                check(climate.humidity(level, plains) == 0.5f, "Native precipitation humidity retained");

                var reload = new ReloadProbe();
                var warmDry = JsonParser.parseString("{\"temperature\":2.0,\"is_dry\":true}");
                var coolWet = JsonParser.parseString("{\"temperature\":-0.5,\"is_dry\":false}");
                var malformed = JsonParser.parseString("{\"is_dry\":true}");
                reload.reload(server, Map.of(TERRALITH, warmDry, PLAINS, coolWet,
                        new ResourceLocation("datapack_only", "invalid"), malformed));
                check(TemperatureDataManager.getBiome(TERRALITH).temperature == 2.0f,
                        "Absent-mod override survives runtime reload");
                check(TemperatureDataManager.getBiome(new ResourceLocation("datapack_only", "invalid")) == null,
                        "Malformed entry rejected without losing valid entries");
                check(climate.temperature(level, plains) == 0.0f && climate.humidity(level, plains) == 0.5f,
                        "Vanilla override supplies cold/wet values");

                reload.reload(server, Map.of(PLAINS, warmDry));
                check(TemperatureDataManager.getBiome(TERRALITH) == null, "Removed overrides cleared on reload");
                check(climate.temperature(level, plains) == 1.0f && climate.humidity(level, plains) == 0.1f,
                        "Override supplies hot/dry values");

                var buffer = PacketByteBufs.create();
                try {
                    SyncTemperatureBiomesPacket.encode(
                            new SyncTemperatureBiomesPacket(Map.of(TERRALITH, fromPack)), buffer);
                    var packet = SyncTemperatureBiomesPacket.decode(buffer);
                    check(buffer.readableBytes() == 0, "Sync packet consumes complete payload");
                    packet.applyToClient();
                    var synced = TemperatureDataManager.getBiome(TERRALITH);
                    check(synced != null && synced.temperature == 1.75f && synced.isDry,
                            "Client application retains datapack-only namespace");
                } finally {
                    buffer.release();
                }

                reload.reload(server, Map.of());
                check(climate.temperature(level, plains) == nativeTemperature,
                        "Removing override restores native temperature");
                System.out.println("BIOME_COMPATIBILITY_CHECK_PASSED");
            } finally {
                server.halt(false);
            }
        });
    }
}
