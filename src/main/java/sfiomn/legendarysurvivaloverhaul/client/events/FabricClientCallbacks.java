package sfiomn.legendarysurvivaloverhaul.client.events;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.HitResult;
import sereneseasons.api.SSItems;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.client.network.FabricClientNetworkHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public final class FabricClientCallbacks {
    private FabricClientCallbacks() {
    }

    public static void register() {
        UseItemCallback.EVENT.register(FabricClientCallbacks::onUseItem);
    }

    private static InteractionResultHolder<net.minecraft.world.item.ItemStack> onUseItem(
            Player player, Level level, InteractionHand hand) {
        if (!level.isClientSide)
            return InteractionResultHolder.pass(player.getItemInHand(hand));

        var stack = player.getItemInHand(hand);
        var item = stack.getItem();

        if (hand == InteractionHand.MAIN_HAND && stack.isEmpty() && shouldApplyThirst(player)
                && Minecraft.getInstance().hitResult != null
                && Minecraft.getInstance().hitResult.getType() == HitResult.Type.MISS) {
            ThirstCapability thirst = CapabilityUtil.getThirstCapability(player);
            if (!thirst.isHydrationLevelAtMax()) {
                JsonThirstBlock fluidThirst = ThirstUtil.getFluidThirstLookedAt(player, 3.0);
                if (fluidThirst != null && (fluidThirst.hydration != 0 || fluidThirst.saturation != 0)) {
                    player.swing(InteractionHand.MAIN_HAND);
                    player.playSound(SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
                    FabricClientNetworkHandler.sendDrinkBlockFluid();
                }
            }
        } else if (LegendarySurvivalOverhaul.sereneSeasonsLoaded && item == SSItems.CALENDAR) {
            player.displayClientMessage(SereneSeasonsUtil.seasonTooltip(player.blockPosition(), level), true);
        } else if (item == Items.CLOCK) {
            player.displayClientMessage(Component.literal(WorldUtil.timeInGame(Minecraft.getInstance())), true);
        } else if (item == Items.COMPASS) {
            String location = ItemUtil.compassLocation(player);
            if (!location.isEmpty())
                player.displayClientMessage(Component.literal(location), true);
        } else if (Config.Baked.showCoordinateOnMap && item == Items.FILLED_MAP) {
            var mapData = MapItem.getSavedData(stack, level);
            if (mapData != null)
                player.displayClientMessage(Component.translatable(
                        "message.legendarysurvivaloverhaul.filled_map.destination",
                        mapData.centerX, mapData.centerZ), true);
        } else if (item == Items.RECOVERY_COMPASS) {
            String deathLocation = ItemUtil.compassDeathLocation(player);
            if (!deathLocation.isEmpty())
                player.displayClientMessage(Component.literal(deathLocation), true);
        }

        return InteractionResultHolder.pass(stack);
    }

    private static boolean shouldApplyThirst(Player player) {
        return !player.isCreative() && !player.isSpectator()
                && Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }
}
