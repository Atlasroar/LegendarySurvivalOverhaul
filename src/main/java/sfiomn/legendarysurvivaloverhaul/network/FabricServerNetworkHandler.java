package sfiomn.legendarysurvivaloverhaul.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.network.packets.BodyPartHealingTimeMessage;
import sfiomn.legendarysurvivaloverhaul.network.packets.DrinkBlockFluidMessage;

public final class FabricServerNetworkHandler {
    public static final ResourceLocation DRINK_BLOCK_FLUID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "drink_block_fluid");
    public static final ResourceLocation BODY_PART_HEALING_TIME =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "body_part_healing_time");

    private FabricServerNetworkHandler() {
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(DRINK_BLOCK_FLUID,
                (server, player, handler, buffer, responseSender) ->
                        server.execute(() -> DrinkBlockFluidMessage.DrinkWaterOnServer(player)));
        ServerPlayNetworking.registerGlobalReceiver(BODY_PART_HEALING_TIME,
                (server, player, handler, buffer, responseSender) -> {
                    BodyPartHealingTimeMessage message = BodyPartHealingTimeMessage.decode(buffer);
                    server.execute(() -> message.handleServer(player));
                });
    }
}
