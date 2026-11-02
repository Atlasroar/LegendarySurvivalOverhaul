package sfiomn.legendarysurvivaloverhaul.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

/** LSO-owned synchronization works across servers and clients with different optional UI mods. */
final class ConfigNetworking {
    static final ResourceLocation CHANNEL = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "config_sync");
    static final int MAX_PACKET_CHARS = 262144;
    private static String lastSnapshot = "";
    private static int ticks;

    private ConfigNetworking() {}

    static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                send(handler.player, FileConfigStore.snapshot(Config.all()).toString()));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            lastSnapshot = "";
            ticks = 0;
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // Includes server-side Fzzy edits and reloads without coupling the fallback to that API.
            if (++ticks % 20 != 0) return;
            String snapshot = FileConfigStore.snapshot(Config.all()).toString();
            if (snapshot.equals(lastSnapshot)) return;
            lastSnapshot = snapshot;
            for (ServerPlayer player : server.getPlayerList().getPlayers()) send(player, snapshot);
        });
    }

    private static void send(ServerPlayer player, String snapshot) {
        if (!ServerPlayNetworking.canSend(player, CHANNEL)) return;
        var buffer = PacketByteBufs.create();
        buffer.writeUtf(snapshot, MAX_PACKET_CHARS);
        ServerPlayNetworking.send(player, CHANNEL, buffer);
    }
}
