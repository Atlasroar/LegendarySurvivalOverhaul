package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;

import java.util.HashMap;
import java.util.Map;

public class SyncTemperatureBiomesPacket
{
	private final Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes;
	private final int size;

	public SyncTemperatureBiomesPacket(Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes)
	{
		this.temperatureBiomes = Map.copyOf(temperatureBiomes);
		this.size = temperatureBiomes.size();
	}

	public static void encode(SyncTemperatureBiomesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureBiomeOverride> e : message.temperatureBiomes.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureBiomeOverride.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureBiomesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureBiomeOverride.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureBiomes.put(key, t));
			}
		}

		return new SyncTemperatureBiomesPacket(temperatureBiomes);
	}
	
	public void applyToClient() {
		TemperatureBiomeListener.acceptServerTemperatureBiomes(temperatureBiomes);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_biomes",
				new SyncTemperatureBiomesPacket(temperatureBiomes), SyncTemperatureBiomesPacket::encode);
	}
}
