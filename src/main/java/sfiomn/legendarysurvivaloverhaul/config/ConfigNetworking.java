package sfiomn.legendarysurvivaloverhaul.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

/** LSO-owned synchronization works across servers and clients with different optional UI mods. */
final class ConfigNetworking {
    static final ResourceLocation CHANNEL = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "config_sync");
    static final int MAX_PACKET_CHARS = 262144;
    private static final FileConfigStore.SnapshotCache SNAPSHOTS = new FileConfigStore.SnapshotCache();
    private static String lastSnapshot = "";
    private static int ticks;

    private ConfigNetworking() {}

    static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                onPlayerJoin(handler.player));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> onServerStopped());
        ServerTickEvents.END_SERVER_TICK.register(ConfigNetworking::onServerTick);
    }

    static void onPlayerJoin(ServerPlayer player) {
        send(player, SNAPSHOTS.get(Config.all()));
    }

    static void onServerStopped() {
        SNAPSHOTS.clear();
        lastSnapshot = "";
        ticks = 0;
    }

    static void onServerTick(MinecraftServer server) {
        // Includes server-side Fzzy edits and reloads without coupling the fallback to that API.
        if (++ticks % 20 != 0) return;
        String snapshot = SNAPSHOTS.get(Config.all());
        if (snapshot.equals(lastSnapshot)) return;
        lastSnapshot = snapshot;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) send(player, snapshot);
    }

    private static void send(ServerPlayer player, String snapshot) {
        if (!ServerPlayNetworking.canSend(player, CHANNEL)) return;
        var buffer = PacketByteBufs.create();
        buffer.writeUtf(snapshot, MAX_PACKET_CHARS);
        ServerPlayNetworking.send(player, CHANNEL, buffer);
    }
}
