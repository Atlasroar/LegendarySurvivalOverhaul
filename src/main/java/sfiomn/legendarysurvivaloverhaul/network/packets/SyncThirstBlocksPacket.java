package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstBlockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncThirstBlocksPacket
{
	private final Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks;
	private final int size;

	public SyncThirstBlocksPacket(Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks)
	{
		this.thirstBlocks = Map.copyOf(thirstBlocks);
		this.size = thirstBlocks.size();
	}

	public static void encode(SyncThirstBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonThirstBlock>> e : message.thirstBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonThirstBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncThirstBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtbSize = buffer.readInt();
			List<JsonThirstBlock> jtbList = new ArrayList<>();
			for (int j = 0; j < jtbSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonThirstBlock.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtbList::add);
				}
			}
			thirstBlocks.put(key, jtbList);
		}

		return new SyncThirstBlocksPacket(thirstBlocks);
	}
	
	public void applyToClient() {
		ThirstBlockListener.acceptServerThirstBlocks(thirstBlocks);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "thirst_blocks",
				new SyncThirstBlocksPacket(thirstBlocks), SyncThirstBlocksPacket::encode);
	}
}
