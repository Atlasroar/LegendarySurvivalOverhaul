package sfiomn.legendarysurvivaloverhaul.network;

import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyDamageHealingConsumableListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartsDamageSourceListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBlockListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableBlockListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureDimensionListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureFuelItemListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureMountListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstBlockListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstConsumableListener;

public final class FabricDataSyncHandler {
    private FabricDataSyncHandler() {
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "sync_" + path);
    }

    public static <T> void send(ServerPlayer player, String path, T packet, BiConsumer<T, FriendlyByteBuf> encoder) {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        encoder.accept(packet, buffer);
        ServerPlayNetworking.send(player, id(path), buffer);
    }

    public static void syncAll(ServerPlayer player) {
        ThirstBlockListener.sendDataToClient(player);
        ThirstConsumableListener.sendDataToClient(player);
        TemperatureBiomeListener.sendDataToClient(player);
        TemperatureBlockListener.sendDataToClient(player);
        TemperatureConsumableListener.sendDataToClient(player);
        TemperatureDimensionListener.sendDataToClient(player);
        TemperatureFuelItemListener.sendDataToClient(player);
        TemperatureItemListener.sendDataToClient(player);
        TemperatureMountListener.sendDataToClient(player);
        BodyDamageHealingConsumableListener.sendDataToClient(player);
        BodyPartsDamageSourceListener.sendDataToClient(player);
        BodyPartResistanceItemListener.sendDataToClient(player);
    }
}
