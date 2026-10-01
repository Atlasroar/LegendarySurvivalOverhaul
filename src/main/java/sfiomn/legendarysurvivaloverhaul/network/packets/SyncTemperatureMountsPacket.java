package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureMountListener;

import java.util.HashMap;
import java.util.Map;

public class SyncTemperatureMountsPacket
{
	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts;
	private final int size;

	public SyncTemperatureMountsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts)
	{
		this.temperatureMounts = Map.copyOf(temperatureMounts);
		this.size = temperatureMounts.size();
	}

	public static void encode(SyncTemperatureMountsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureMounts.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureMountsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureMounts.put(key, t));
			}
		}

		return new SyncTemperatureMountsPacket(temperatureMounts);
	}
	
	public void applyToClient() {
		TemperatureMountListener.acceptServerTemperatureMounts(temperatureMounts);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_mounts",
				new SyncTemperatureMountsPacket(temperatureMounts), SyncTemperatureMountsPacket::encode);
	}
}
