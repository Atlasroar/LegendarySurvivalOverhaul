package sfiomn.legendarysurvivaloverhaul.client.network;

import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncBodyDamageHealingConsumablesPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncBodyPartResistanceItemsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncBodyPartsDamageSourcesPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureBiomesPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureBlocksPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureConsumableBlocksPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureConsumablesPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureDimensionsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureFuelItemsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureItemsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncTemperatureMountsPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncThirstBlocksPacket;
import sfiomn.legendarysurvivaloverhaul.network.packets.SyncThirstConsumablesPacket;

public final class FabricDataSyncReceiver {
    private FabricDataSyncReceiver() {
    }

    public static void register() {
        register("thirst_blocks", SyncThirstBlocksPacket::decode, SyncThirstBlocksPacket::applyToClient);
        register("thirst_consumables", SyncThirstConsumablesPacket::decode, SyncThirstConsumablesPacket::applyToClient);
        register("temperature_biomes", SyncTemperatureBiomesPacket::decode, SyncTemperatureBiomesPacket::applyToClient);
        register("temperature_blocks", SyncTemperatureBlocksPacket::decode, SyncTemperatureBlocksPacket::applyToClient);
        register("temperature_consumables", SyncTemperatureConsumablesPacket::decode, SyncTemperatureConsumablesPacket::applyToClient);
        register("temperature_consumable_blocks", SyncTemperatureConsumableBlocksPacket::decode, SyncTemperatureConsumableBlocksPacket::applyToClient);
        register("temperature_dimensions", SyncTemperatureDimensionsPacket::decode, SyncTemperatureDimensionsPacket::applyToClient);
        register("temperature_fuel_items", SyncTemperatureFuelItemsPacket::decode, SyncTemperatureFuelItemsPacket::applyToClient);
        register("temperature_items", SyncTemperatureItemsPacket::decode, SyncTemperatureItemsPacket::applyToClient);
        register("temperature_mounts", SyncTemperatureMountsPacket::decode, SyncTemperatureMountsPacket::applyToClient);
        register("body_damage_healing_consumables", SyncBodyDamageHealingConsumablesPacket::decode, SyncBodyDamageHealingConsumablesPacket::applyToClient);
        register("body_parts_damage_sources", SyncBodyPartsDamageSourcesPacket::decode, SyncBodyPartsDamageSourcesPacket::applyToClient);
        register("body_part_resistance_items", SyncBodyPartResistanceItemsPacket::decode, SyncBodyPartResistanceItemsPacket::applyToClient);
    }

    private static <T> void register(String path, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        ResourceLocation channel = FabricDataSyncHandler.id(path);
        ClientPlayNetworking.registerGlobalReceiver(channel,
                (client, listener, buffer, responseSender) -> {
                    T packet = decoder.apply(buffer);
                    client.execute(() -> handler.accept(packet));
                });
    }
}
