package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;

import java.util.HashMap;
import java.util.Map;

public class SyncTemperatureItemsPacket
{
	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureItems;
	private final int size;

	public SyncTemperatureItemsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureItems)
	{
		this.temperatureItems = Map.copyOf(temperatureItems);
		this.size = temperatureItems.size();
	}

	public static void encode(SyncTemperatureItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureItems.put(key, t));
			}
		}

		return new SyncTemperatureItemsPacket(temperatureItems);
	}
	
	public void applyToClient() {
		TemperatureItemListener.acceptServerTemperatureItems(temperatureItems);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> temperatureItems) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_items",
				new SyncTemperatureItemsPacket(temperatureItems), SyncTemperatureItemsPacket::encode);
	}
}
