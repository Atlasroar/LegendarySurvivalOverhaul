package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBlockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncTemperatureBlocksPacket
{
	private final Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks;
	private final int size;

	public SyncTemperatureBlocksPacket(Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks)
	{
		this.temperatureBlocks = Map.copyOf(temperatureBlocks);
		this.size = temperatureBlocks.size();
	}

	public static void encode(SyncTemperatureBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureBlock>> e : message.temperatureBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtbSize = buffer.readInt();
			List<JsonTemperatureBlock> jtbList = new ArrayList<>();
			for (int j = 0; j < jtbSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureBlock.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtbList::add);
				}
			}
			temperatureBlocks.put(key, jtbList);
		}
		return new SyncTemperatureBlocksPacket(temperatureBlocks);
	}
	
	public void applyToClient() {
		TemperatureBlockListener.acceptServerTemperatureBlocks(temperatureBlocks);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_blocks",
				new SyncTemperatureBlocksPacket(temperatureBlocks), SyncTemperatureBlocksPacket::encode);
	}
}
