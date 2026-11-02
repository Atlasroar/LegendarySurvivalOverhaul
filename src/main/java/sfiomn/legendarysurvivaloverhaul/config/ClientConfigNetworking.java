package sfiomn.legendarysurvivaloverhaul.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

/** Isolated from the dedicated-server class loading path. */
@Environment(EnvType.CLIENT)
final class ClientConfigNetworking {
    private static JsonObject local;

    private ClientConfigNetworking() {}

    static void register() {
        // Capture before either LSO or Fzzy can receive server values.
        ClientPlayConnectionEvents.INIT.register((handler, client) ->
                local = FileConfigStore.snapshot(Config.all()));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (local == null) return;
            FileConfigStore.apply(Config.all(), local);
            updateEditor();
            Config.all().forEach(Config::bake);
            local = null;
        });
        ClientPlayNetworking.registerGlobalReceiver(ConfigNetworking.CHANNEL, (client, handler, buffer, sender) -> {
            String payload = buffer.readUtf(ConfigNetworking.MAX_PACKET_CHARS);
            client.execute(() -> {
                if (client.getConnection() != handler) return;
                try {
                    FileConfigStore.apply(Config.all(), JsonParser.parseString(payload).getAsJsonObject());
                    updateEditor();
                    Config.all().forEach(Config::bake);
                } catch (RuntimeException e) {
                    LegendarySurvivalOverhaul.LOGGER.error("Rejected invalid server configuration", e);
                }
            });
        });
    }

    private static void updateEditor() {
        if (Config.fzzyAvailable())
            sfiomn.legendarysurvivaloverhaul.config.fzzy.FzzyConfigBridge.updateFromData();
    }
}
