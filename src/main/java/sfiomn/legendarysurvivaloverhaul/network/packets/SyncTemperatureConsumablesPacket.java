package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncTemperatureConsumablesPacket
{
	private final Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables;
	private final int size;

	public SyncTemperatureConsumablesPacket(Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables)
	{
		this.temperatureConsumables = Map.copyOf(temperatureConsumables);
		this.size = temperatureConsumables.size();
	}

	public static void encode(SyncTemperatureConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumable>> e : message.temperatureConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonTemperatureConsumable> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			temperatureConsumables.put(key, jtcList);
		}

		return new SyncTemperatureConsumablesPacket(temperatureConsumables);
	}
	
	public void applyToClient() {
		TemperatureConsumableListener.acceptServerTemperatureConsumables(temperatureConsumables);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_consumables",
				new SyncTemperatureConsumablesPacket(temperatureConsumables), SyncTemperatureConsumablesPacket::encode);
	}
}
