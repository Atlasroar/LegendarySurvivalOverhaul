package sfiomn.legendarysurvivaloverhaul.client.events;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
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
import sfiomn.legendarysurvivaloverhaul.client.ClientHooks;
import sfiomn.legendarysurvivaloverhaul.client.effects.TemperatureBreathEffect;
import sfiomn.legendarysurvivaloverhaul.client.screens.WarningDataPackScreen;
import sfiomn.legendarysurvivaloverhaul.client.sounds.TemperatureBreathSound;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.config.json_old.JsonConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.client.network.FabricClientNetworkHandler;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderBodyDamageGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderBlurOverlay;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderThirstGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderWetnessGui;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public final class FabricClientCallbacks {
    private static boolean hasOpened;
    private static int warningPageDelay = 40;

    private FabricClientCallbacks() {
    }

    public static void register() {
        UseItemCallback.EVENT.register(FabricClientCallbacks::onUseItem);
        ClientTickEvents.END_CLIENT_TICK.register(FabricClientCallbacks::onEndClientTick);
        WorldRenderEvents.END.register(context -> {
            Minecraft client = Minecraft.getInstance();
            Player player = client.player;
            if (player != null && Config.Baked.thirstEnabled && Config.Baked.lowHydrationEffect
                    && shouldApplyThirst(player)) {
                RenderBlurOverlay.render(player);
            } else {
                RenderBlurOverlay.stop();
            }
        });
        FabricHudCallbacks.register();
    }

    private static void onEndClientTick(Minecraft client) {
        Player player = client.player;
        if (!client.isPaused() && player != null) {
            if (Config.Baked.temperatureEnabled) {
                RenderTemperatureGui.updateTimer();
                if (Config.Baked.coldBreathEffectThreshold != -1000)
                    TemperatureBreathEffect.tickPlay(player);
                if (Config.Baked.breathingSoundEnabled)
                    TemperatureBreathSound.tickPlay(player);
            }

            if (Config.Baked.localizedBodyDamageEnabled) {
                RenderBodyDamageGui.updateFlashingTimer();
                if (KeyMappingRegistry.showBodyHealth.consumeClick())
                    ClientHooks.openBodyHealthScreen(player);
            }
            if (Config.Baked.thirstEnabled)
                RenderThirstGui.updateTimer();
            if (Config.Baked.thirstEnabled && Config.Baked.lowHydrationEffect && shouldApplyThirst(player))
                RenderBlurOverlay.updateBlurIntensity(player);
            else
                RenderBlurOverlay.updateBlurIntensity(null);
            if (Config.Baked.wetnessEnabled)
                RenderWetnessGui.updateTimer();

            if (player.tickCount % 10 == 0)
                TrinketsUtil.isThermometerEquipped =
                        TrinketsUtil.isTrinketItemEquipped(player, ItemRegistry.THERMOMETER.get());
        }

        if (client.screen instanceof TitleScreen) {
            warningPageDelay = Math.max(0, warningPageDelay - 1);
            if (warningPageDelay == 0 && !hasOpened
                    && JsonConfigRegistration.customDatapackFolder.toFile().exists()) {
                client.setScreen(new WarningDataPackScreen());
                hasOpened = true;
            }
        }

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
