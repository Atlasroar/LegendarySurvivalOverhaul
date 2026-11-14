package sfiomn.legendarysurvivaloverhaul.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ConfigNetworkingCheck {
    private static final Gson GSON = new Gson();
    private static boolean active;
    private static int serializations;
    private static final List<Message> SENT = new ArrayList<>();
    private record Message(ServerPlayer player, String snapshot) {}

    public static void recordSerialization() {
        if (active)
            serializations++;
    }

    public static boolean captureSend(ServerPlayer player, String snapshot) {
        if (!active)
            return false;
        SENT.add(new Message(player, snapshot));
        return true;
    }

    private static void reset() {
        serializations = 0;
        SENT.clear();
    }

    private static void ticks(MinecraftServer server, int count) {
        for (int i = 0; i < count; i++)
            ConfigNetworking.onServerTick(server);
    }

    private static void require(boolean condition, String message) {
        if (!condition)
            throw new AssertionError(message);
    }

    private static void checkMessages(int count) {
        require(SENT.size() == count, "Expected " + count + " config send attempts, got " + SENT.size());
        String expected = referenceSnapshot();
        for (Message message : SENT)
            require(message.snapshot.equals(expected), "Config payload matches original wire representation");
    }

    public static void run(MinecraftServer server) throws ReflectiveOperationException {
        JsonObject original = FileConfigStore.snapshot(Config.all());
        var originalCount = Config.COMMON.routinePacketSync;
        boolean originalClient = Config.CLIENT.foodSaturationDisplayed.get();
        ServerPlayer first = new ServerPlayer(server, server.overworld(),
                new GameProfile(UUID.randomUUID(), "ConfigFirst"));
        ServerPlayer second = new ServerPlayer(server, server.overworld(),
                new GameProfile(UUID.randomUUID(), "ConfigSecond"));
        var players = server.getPlayerList().getPlayers();
        players.add(first);
        ConfigNetworking.onServerStopped();
        active = true;
        try {
            reset();
            ConfigNetworking.onPlayerJoin(first);
            require(serializations == 1, "Initial join serializes fresh settings");
            checkMessages(1);
            require(SENT.get(0).player == first, "Join targets joining player");
            reset();
            ticks(server, 19);
            require(serializations == 0 && SENT.isEmpty(), "Existing 20-tick cadence is preserved");
            ticks(server, 1);
            require(serializations == 0, "Initial broadcast reuses join serialization");
            checkMessages(1);
            reset();
            ticks(server, 400);
            require(serializations == 0 && SENT.isEmpty(), "20 unchanged polls skip serialization and sends");
            System.out.println("CONFIG_UNCHANGED_SERIALIZATIONS: 20 -> " + serializations);

            Config.CLIENT.foodSaturationDisplayed.validateAndSet(!originalClient);
            ticks(server, 20);
            require(serializations == 0 && SENT.isEmpty(), "Client-only edits do not rebuild gameplay JSON");
            Config.COMMON.routinePacketSync.validateAndSet(nextCount());
            ticks(server, 19);
            require(serializations == 0 && SENT.isEmpty(), "Edits do not change polling latency");
            ConfigNetworking.onPlayerJoin(second);
            require(serializations == 1, "Join immediately sees edits before next poll");
            checkMessages(1);
            require(SENT.get(0).player == second, "Fresh snapshot reaches new player only");
            players.add(second);
            reset();
            ticks(server, 1);
            require(serializations == 0, "Broadcast reuses fresh joining-player snapshot");
            checkMessages(2);

            reset();
            Config.COMMON.routinePacketSync.validateAndSet(Config.COMMON.routinePacketSync.get());
            Config.bake(Config.COMMON);
            ticks(server, 20);
            require(serializations == 0 && SENT.isEmpty(), "Equal edits and baking do not serialize again");
            Config.COMMON.routinePacketSync = new ConfigValue.ValidatedInt(nextCount(),
                    originalCount.max, originalCount.min, originalCount.widget);
            ticks(server, 20);
            require(serializations == 1, "Replacing a public setting field is detected");
            checkMessages(2);
            reset();
            Config.TRINKETS.thermometerSlots.validateAndSet(
                    Config.TRINKETS.thermometerSlots.get().isEmpty() ? List.of("legs/belt") : List.of());
            ticks(server, 20);
            require(serializations == 1, "Validated list edits rebuild once");
            checkMessages(2);

            if (Config.fzzyAvailable()) {
                reset();
                sfiomn.legendarysurvivaloverhaul.config.fzzy.FzzyCacheCheck.updateFromEditor(nextCount());
                ticks(server, 20);
                require(serializations == 1, "Real Fzzy update hook refreshes cached JSON");
                checkMessages(2);
                System.out.println("CONFIG_FZZY_CACHE_CHECK_PASSED");
            }

            reset();
            JsonObject invalid = original.deepCopy();
            invalid.getAsJsonObject("common").addProperty("routinePacketSync", "invalid");
            try {
                FileConfigStore.apply(Config.all(), invalid);
                throw new AssertionError("Malformed settings accepted");
            } catch (IllegalArgumentException expected) {
                ticks(server, 20);
                require(serializations == 0 && SENT.isEmpty(), "Rejected update leaves cached snapshot unchanged");
            }
            FileConfigStore.apply(Config.all(), original);
            ticks(server, 20);
            require(serializations == 1, "Restoration updates the cached snapshot");
            checkMessages(2);
            reset();
            ConfigNetworking.onServerStopped();
            ticks(server, 19);
            require(serializations == 0 && SENT.isEmpty(), "Restart resets poll counter");
            ticks(server, 1);
            require(serializations == 1, "Restart rebuilds and broadcasts even with equal settings");
            checkMessages(2);
            benchmark();
            System.out.println("CONFIG_CACHE_CHECK_PASSED");
        } finally {
            active = false;
            players.remove(first);
            players.remove(second);
            Config.COMMON.routinePacketSync = originalCount;
            Config.CLIENT.foodSaturationDisplayed.validateAndSet(originalClient);
            FileConfigStore.apply(Config.all(), original);
            Config.all().forEach(Config::bake);
            if (Config.fzzyAvailable())
                sfiomn.legendarysurvivaloverhaul.config.fzzy.FzzyCacheCheck.restoreEditor();
            ConfigNetworking.onServerStopped();
        }
    }

    private static int nextCount() {
        var count = Config.COMMON.routinePacketSync;
        return count.get().equals(count.max) ? count.min : count.get() + 1;
    }

    private static String referenceSnapshot() {
        JsonObject root = new JsonObject();
        for (ConfigData config : Config.all()) {
            if (config instanceof ClientConfig) continue;
            JsonObject fields = new JsonObject();
            config.fields().forEach((name, value) -> fields.add(name, GSON.toJsonTree(value.get())));
            root.add(config.name, fields);
        }
        return root.toString();
    }

    private static void benchmark() {
        FileConfigStore.SnapshotCache cache = new FileConfigStore.SnapshotCache();
        for (int i = 0; i < 200; i++) {
            cache.get(Config.all());
            referenceSnapshot();
        }
        long reference = 0;
        long cached = 0;
        for (int round = 0; round < 4; round++) {
            for (int phase = 0; phase < 2; phase++) {
                boolean original = (round + phase) % 2 == 0;
                long start = System.nanoTime();
                for (int i = 0; i < 500; i++) {
                    String snapshot = original ? referenceSnapshot() : cache.get(Config.all());
                    require(!snapshot.isEmpty(), "Snapshot exists during timing comparison");
                }
                if (original)
                    reference += System.nanoTime() - start;
                else
                    cached += System.nanoTime() - start;
            }
        }
        System.out.printf("CONFIG_POLL_TIMING: reference_ms=%.2f cached_ms=%.2f%n",
                reference / 1000000.0, cached / 1000000.0);
    }
}
