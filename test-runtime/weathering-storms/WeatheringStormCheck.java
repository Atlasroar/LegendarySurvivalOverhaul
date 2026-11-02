import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import sfiomn.legendarysurvivaloverhaul.common.TickPhase;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.ModCapabilities;
import sfiomn.legendarysurvivaloverhaul.common.integration.weathering.WeatheringStorms;
import sfiomn.legendarysurvivaloverhaul.common.integration.weathering.WeatheringStorms.Exposure;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.UUID;

public final class WeatheringStormCheck implements ModInitializer {
    static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                ServerLevel level = server.overworld();
                ServerPlayer player = new ServerPlayer(server, level,
                        new GameProfile(UUID.randomUUID(), "StormCheck"));
                player.connection = new ServerGamePacketListenerImpl(server, new Connection(PacketFlow.SERVERBOUND), player);
                player.setPos(8.5, 200, 8.5);
                level.getChunk(0, 0);
                TemperatureCapability cap = CapabilityUtil.getTempCapability(player);
                Config.Baked.temperatureEnabled = true;
                Config.Baked.immersiveWeatheringStormsEnabled = true;
                Config.Baked.tempTickTime = 5;
                Config.Baked.minTemperatureModification = 0.5;
                Config.Baked.maxTemperatureModification = 0.5;
                Config.Baked.coldTemperatureSecondaryEffects = false;
                Config.Baked.heatTemperatureSecondaryEffects = false;
                BlockPos eyes = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
                roof(level, eyes, 1, Blocks.AIR);
                roof(level, eyes, 10, Blocks.AIR);
                roof(level, eyes, 11, Blocks.AIR);
                storm(level, true, true);

                if (!FabricLoader.getInstance().isModLoaded("immersive_weathering")) {
                    check(WeatheringStorms.exposure(player) == Exposure.NONE, "Missing IW is inert");
                    WeatheringStorms.onFreezingWindHit(player);
                    tick(cap, player);
                    check(!player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "Missing IW has no wind penalty");
                    System.out.println("WEATHERING_STORM_CHECK_PASSED absent");
                    return;
                }

                biome(level, Biomes.SNOWY_PLAINS);
                check(WeatheringStorms.exposure(player) == Exposure.SNOW, "Thunder in snowy biome detected");
                cap.setTemperatureLevel(20);
                tick(cap, player);
                check(cap.getTemperatureLevel() == 19, "Snow cools by exactly twice the 0.5 baseline");
                check(cap.getTargetTemperatureLevel() <= 5, "Snow forces frostbite target");
                check(player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "Snow grants hunger above frostbite");
                float coldTarget = cap.getTargetTemperatureLevel();
                cap.setTemperatureLevel(coldTarget + 0.25f);
                tick(cap, player);
                check(cap.getTemperatureLevel() == coldTarget, "Doubled cooling never overshoots target");
                cap.setTemperatureLevel(20);
                roof(level, eyes, 10, Blocks.STONE);
                check(WeatheringStorms.sheltered(level, eyes), "Full 3x3 roof at height 10 shelters");
                tick(cap, player);
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Shelter stops snow");
                check(!player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "Shelter removes storm hunger");
                level.setBlockAndUpdate(eyes.offset(1, 10, 1), Blocks.AIR.defaultBlockState());
                check(!WeatheringStorms.sheltered(level, eyes), "One uncovered corner is not shelter");
                roof(level, eyes, 10, Blocks.AIR);
                roof(level, eyes, 11, Blocks.STONE);
                check(!WeatheringStorms.sheltered(level, eyes), "Roof above 10 blocks is not shelter");
                check(WeatheringStorms.exposure(player) == Exposure.SNOW, "High roof does not bypass shelter limit");
                roof(level, eyes, 11, Blocks.AIR);
                roof(level, eyes, 1, Blocks.GLASS);
                check(!WeatheringStorms.sheltered(level, eyes), "Transparent sky-visible glass is not shelter");
                roof(level, eyes, 1, Blocks.AIR);
                player.addEffect(new MobEffectInstance(MobEffectRegistry.HOT_DRINk.get(), 600));
                tick(cap, player);
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Hot Drink blocks snow");
                check(!player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "Hot Drink removes storm hunger");
                player.removeEffect(MobEffectRegistry.HOT_DRINk.get());
                player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_DRINK.get(), 600));
                check(WeatheringStorms.exposure(player) == Exposure.SNOW, "Cold Drink does not block snow");
                player.removeEffect(MobEffectRegistry.COLD_DRINK.get());
                storm(level, true, false);
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Ordinary snowfall is not snowstorm");
                storm(level, true, true);

                biome(level, Biomes.DESERT);
                cap.setTemperatureLevel(20);
                tick(cap, player);
                check(WeatheringStorms.exposure(player) == Exposure.SAND, "Tagged desert rain detected");
                check(cap.getTemperatureLevel() == 21, "Sand heats by exactly twice the 0.5 baseline");
                check(cap.getTargetTemperatureLevel() >= 35, "Sand forces heatstroke target");
                check(player.hasEffect(MobEffectRegistry.HEAT_THIRST.get()), "Sand grants Heat Thirst early");
                check(!player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "No stale snow hunger after biome change");
                float hotTarget = cap.getTargetTemperatureLevel();
                cap.setTemperatureLevel(hotTarget - 0.25f);
                tick(cap, player);
                check(cap.getTemperatureLevel() == hotTarget, "Doubled heating never overshoots target");
                cap.setTemperatureLevel(20);
                player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_DRINK.get(), 600));
                tick(cap, player);
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Cold Drink blocks sand");
                check(!player.hasEffect(MobEffectRegistry.HEAT_THIRST.get()), "Cold Drink removes storm thirst");
                player.removeEffect(MobEffectRegistry.COLD_DRINK.get());
                player.addEffect(new MobEffectInstance(MobEffectRegistry.HOT_DRINk.get(), 600));
                check(WeatheringStorms.exposure(player) == Exposure.SAND, "Hot Drink does not block sand");
                player.removeEffect(MobEffectRegistry.HOT_DRINk.get());
                roof(level, eyes, 1, Blocks.STONE);
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Low shelter also stops sand");
                roof(level, eyes, 1, Blocks.AIR);
                Config.Baked.immersiveWeatheringStormsEnabled = false;
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Standalone integration switch disables storms");
                tick(cap, player);
                check(!player.hasEffect(MobEffectRegistry.HEAT_THIRST.get()), "Integration switch clears storm thirst");
                Config.Baked.immersiveWeatheringStormsEnabled = true;
                player.addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY.get(), 600));
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "General immunity respected");
                player.removeEffect(MobEffectRegistry.TEMPERATURE_IMMUNITY.get());
                tick(cap, player);
                cap.readNBT(cap.writeNBT());
                Config.Baked.temperatureEnabled = false;
                ModCapabilities.onPlayerTick(player, TickPhase.END);
                check(!player.hasEffect(MobEffectRegistry.HEAT_THIRST.get()), "Disabling temperature clears storm-only thirst");
                Config.Baked.temperatureEnabled = true;
                storm(level, false, false);
                WeatheringWindAssertions.hit(level, player);
                MobEffectInstance hunger = player.getEffect(MobEffectRegistry.COLD_HUNGER.get());
                check(hunger != null && hunger.getDuration() == 200, "Real wind impact grants exactly 10 seconds");
                for (int i = 0; i < 199; i++) {
                    hunger.tick(player, () -> {});
                    cap.tickUpdate(player, level, TickPhase.END);
                    check(player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "Ambient updates preserve finite wind hunger");
                }
                check(hunger.getDuration() == 1, "Wind lasts 199 elapsed ticks");
                if (!hunger.tick(player, () -> {}))
                    player.removeEffect(MobEffectRegistry.COLD_HUNGER.get());
                cap.tickUpdate(player, level, TickPhase.END);
                check(!player.hasEffect(MobEffectRegistry.COLD_HUNGER.get()), "Wind hunger expires after tick 200");

                biome(level, Biomes.SNOWY_PLAINS);
                storm(level, true, true);
                tick(cap, player);
                WeatheringStorms.onFreezingWindHit(player);
                tick(cap, player);
                cap.readNBT(cap.writeNBT());
                storm(level, false, false);
                tick(cap, player);
                check(player.getEffect(MobEffectRegistry.COLD_HUNGER.get()).getDuration() == 198,
                        "Wind duration survives leaving an overlapping snowstorm");
                Config.Baked.coldTemperatureSecondaryEffects = true;
                cap.setTemperatureLevel(4);
                tick(cap, player);
                check(player.getEffect(MobEffectRegistry.COLD_HUNGER.get()).isInfiniteDuration(),
                        "Ordinary frostbite hunger remains independent");
                Config.Baked.coldTemperatureSecondaryEffects = false;
                cap.setTemperatureLevel(20);
                storm(level, true, true);
                tick(cap, player);
                cap.readNBT(cap.writeNBT());
                player.setGameMode(net.minecraft.world.level.GameType.CREATIVE);
                ModCapabilities.onPlayerTick(player, TickPhase.END);
                check(player.getEffect(MobEffectRegistry.COLD_HUNGER.get()) == null
                                || !player.getEffect(MobEffectRegistry.COLD_HUNGER.get()).isInfiniteDuration(),
                        "Switching to Creative removes storm-only continuous hunger");
                check(WeatheringStorms.exposure(player) == Exposure.NONE, "Creative skips storm penalties");
                System.out.println("WEATHERING_STORM_CHECK_PASSED present");
            } finally {
                server.halt(false);
            }
        });
    }

    static void tick(TemperatureCapability cap, ServerPlayer player) {
        cap.setTemperatureTickTimer(4);
        cap.tickUpdate(player, player.level(), TickPhase.END);
    }

    static void roof(ServerLevel level, BlockPos eyes, int height, net.minecraft.world.level.block.Block block) {
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++)
                level.setBlockAndUpdate(eyes.offset(x, height, z), block.defaultBlockState());
    }

    static void storm(ServerLevel level, boolean rain, boolean thunder) {
        level.setWeatherParameters(0, 6000, rain, thunder);
        level.setRainLevel(rain ? 1 : 0);
        level.setThunderLevel(thunder ? 1 : 0);
    }

    static void biome(ServerLevel level, net.minecraft.resources.ResourceKey<net.minecraft.world.level.biome.Biome> key) {
        var biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(key);
        level.getChunk(0, 0).fillBiomesFromNoise((x, y, z, sampler) -> biome, level.getChunkSource().randomState().sampler());
    }
}
