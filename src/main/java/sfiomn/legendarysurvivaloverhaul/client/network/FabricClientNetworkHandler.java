package sfiomn.legendarysurvivaloverhaul.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.network.FabricServerNetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.BodyPartHealingTimeMessage;
import sfiomn.legendarysurvivaloverhaul.network.packets.DrinkBlockFluidMessage;

public final class FabricClientNetworkHandler {
    private FabricClientNetworkHandler() {
    }

    public static void sendDrinkBlockFluid() {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        DrinkBlockFluidMessage.encode(new DrinkBlockFluidMessage(), buffer);
        ClientPlayNetworking.send(FabricServerNetworkHandler.DRINK_BLOCK_FLUID, buffer);
    }

    public static void sendBodyPartHealing(
            BodyPartEnum bodyPart, String healingItem, InteractionHand hand, boolean consumeItem, boolean applyEffect) {
        BodyPartHealingTimeMessage message =
                new BodyPartHealingTimeMessage(bodyPart, healingItem, hand, consumeItem, applyEffect);
        FriendlyByteBuf buffer = PacketByteBufs.create();
        BodyPartHealingTimeMessage.encode(message, buffer);
        ClientPlayNetworking.send(FabricServerNetworkHandler.BODY_PART_HEALING_TIME, buffer);
    }
}
