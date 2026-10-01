package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstConsumableListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncThirstConsumablesPacket
{
	private final Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables;
	private final int size;

	public SyncThirstConsumablesPacket(Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables)
	{
		this.thirstConsumables = Map.copyOf(thirstConsumables);
		this.size = thirstConsumables.size();
	}

	public static void encode(SyncThirstConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonThirstConsumable>> e : message.thirstConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonThirstConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncThirstConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonThirstConsumable> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonThirstConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			thirstConsumables.put(key, jtcList);
		}

		return new SyncThirstConsumablesPacket(thirstConsumables);
	}
	
	public void applyToClient() {
		ThirstConsumableListener.acceptServerThirstConsumables(thirstConsumables);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "thirst_consumables",
				new SyncThirstConsumablesPacket(thirstConsumables), SyncThirstConsumablesPacket::encode);
	}
}
