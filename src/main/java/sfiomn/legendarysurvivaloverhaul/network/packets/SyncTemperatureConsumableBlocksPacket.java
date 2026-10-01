package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableBlockListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncTemperatureConsumableBlocksPacket
{
	private final Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks;
	private final int size;

	public SyncTemperatureConsumableBlocksPacket(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks)
	{
		this.temperatureConsumableBlocks = Map.copyOf(temperatureConsumableBlocks);
		this.size = temperatureConsumableBlocks.size();
	}

	public static void encode(SyncTemperatureConsumableBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumableBlock>> e : message.temperatureConsumableBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureConsumableBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureConsumableBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonTemperatureConsumableBlock> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureConsumableBlock.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			temperatureConsumables.put(key, jtcList);
		}

		return new SyncTemperatureConsumableBlocksPacket(temperatureConsumables);
	}
	
	public void applyToClient() {
		TemperatureConsumableBlockListener.acceptServerTemperatureConsumableBlocks(temperatureConsumableBlocks);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_consumable_blocks",
				new SyncTemperatureConsumableBlocksPacket(temperatureConsumableBlocks), SyncTemperatureConsumableBlocksPacket::encode);
	}
}
