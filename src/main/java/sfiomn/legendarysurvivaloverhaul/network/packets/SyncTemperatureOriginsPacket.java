package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureOriginListener;

import java.util.HashMap;
import java.util.Map;

public class SyncTemperatureOriginsPacket
{
	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins;
	private final int size;

	public SyncTemperatureOriginsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins)
	{
		this.temperatureOrigins = Map.copyOf(temperatureOrigins);
		this.size = temperatureOrigins.size();
	}

	public static void encode(SyncTemperatureOriginsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureOrigins.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureOriginsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureOrigins.put(key, t));
			}
		}

		return new SyncTemperatureOriginsPacket(temperatureOrigins);
	}
	
	public void applyToClient() {
		TemperatureOriginListener.acceptServerTemperatureOrigins(temperatureOrigins);
	}

	public static void sendTo(net.minecraft.server.level.ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins) {
		sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler.send(
				player, "temperature_origins",
				new SyncTemperatureOriginsPacket(temperatureOrigins), SyncTemperatureOriginsPacket::encode);
	}
}
