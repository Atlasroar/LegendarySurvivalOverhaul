package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyDamageHealingConsumableListener;

import java.util.HashMap;
import java.util.Map;

public class SyncBodyDamageHealingConsumablesPacket
{
	private final Map<ResourceLocation, JsonHealingConsumable> healingConsumables;
	private final int size;

	public SyncBodyDamageHealingConsumablesPacket(Map<ResourceLocation, JsonHealingConsumable> healingConsumables)
	{
		this.healingConsumables = Map.copyOf(healingConsumables);
		this.size = healingConsumables.size();
	}

	public static void encode(SyncBodyDamageHealingConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonHealingConsumable> e : message.healingConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonHealingConsumable.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyDamageHealingConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonHealingConsumable> healingConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonHealingConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> healingConsumables.put(key, t));
			}
		}

		return new SyncBodyDamageHealingConsumablesPacket(healingConsumables);
	}
	
	public void applyToClient() {
		BodyDamageHealingConsumableListener.acceptServerHealingConsumables(healingConsumables);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonHealingConsumable> healingConsumables) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "body_damage_healing_consumables",
				new SyncBodyDamageHealingConsumablesPacket(healingConsumables), SyncBodyDamageHealingConsumablesPacket::encode);
	}
}
