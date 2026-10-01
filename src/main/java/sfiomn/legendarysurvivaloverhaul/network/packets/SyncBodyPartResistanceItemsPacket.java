package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;

import java.util.HashMap;
import java.util.Map;

public class SyncBodyPartResistanceItemsPacket
{
	private final Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems;
	private final int size;

	public SyncBodyPartResistanceItemsPacket(Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems)
	{
		this.bodyPartResistanceItems = Map.copyOf(bodyPartResistanceItems);
		this.size = bodyPartResistanceItems.size();
	}

	public static void encode(SyncBodyPartResistanceItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonBodyPartResistance> e : message.bodyPartResistanceItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonBodyPartResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyPartResistanceItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonBodyPartResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> bodyPartResistanceItems.put(key, t));
			}
		}

		return new SyncBodyPartResistanceItemsPacket(bodyPartResistanceItems);
	}
	
	public void applyToClient() {
		BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(bodyPartResistanceItems);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "body_part_resistance_items",
				new SyncBodyPartResistanceItemsPacket(bodyPartResistanceItems), SyncBodyPartResistanceItemsPacket::encode);
	}
}
