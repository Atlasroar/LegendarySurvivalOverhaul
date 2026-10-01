package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureFuelItemListener;

import java.util.HashMap;
import java.util.Map;

public class SyncTemperatureFuelItemsPacket
{
	private final Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems;
	private final int size;

	public SyncTemperatureFuelItemsPacket(Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems)
	{
		this.temperatureFuelItems = Map.copyOf(temperatureFuelItems);
		this.size = temperatureFuelItems.size();
	}

	public static void encode(SyncTemperatureFuelItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureFuelItem> e : message.temperatureFuelItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureFuelItem.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureFuelItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureFuelItem.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureFuelItems.put(key, t));
			}
		}

		return new SyncTemperatureFuelItemsPacket(temperatureFuelItems);
	}
	
	public void applyToClient() {
		TemperatureFuelItemListener.acceptServerTemperatureFuelItems(temperatureFuelItems);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_fuel_items",
				new SyncTemperatureFuelItemsPacket(temperatureFuelItems), SyncTemperatureFuelItemsPacket::encode);
	}
}
