package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartsDamageSourceListener;

import java.util.HashMap;
import java.util.Map;

public class SyncBodyPartsDamageSourcesPacket
{
	private final Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources;
	private final int size;

	public SyncBodyPartsDamageSourcesPacket(Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources)
	{
		this.damageSources = Map.copyOf(damageSources);
		this.size = damageSources.size();
	}

	public static void encode(SyncBodyPartsDamageSourcesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonBodyPartsDamageSource> e : message.damageSources.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonBodyPartsDamageSource.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyPartsDamageSourcesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonBodyPartsDamageSource.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> damageSources.put(key, t));
			}
		}

		return new SyncBodyPartsDamageSourcesPacket(damageSources);
	}
	
	public void applyToClient() {
		BodyPartsDamageSourceListener.acceptServerDamageSources(damageSources);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "body_parts_damage_sources",
				new SyncBodyPartsDamageSourcesPacket(damageSources), SyncBodyPartsDamageSourcesPacket::encode);
	}
}
