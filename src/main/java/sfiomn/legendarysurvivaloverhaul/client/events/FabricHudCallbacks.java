package sfiomn.legendarysurvivaloverhaul.client.events;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderBodyDamageGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderHealthGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderThirstGui;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureOverlay;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderWetnessGui;
import sfiomn.legendarysurvivaloverhaul.common.integration.overflowingbars.OverflowingBarsUtil;

public final class FabricHudCallbacks {
    private FabricHudCallbacks() {
    }

    public static void register() {
        HudRenderCallback.EVENT.register(FabricHudCallbacks::render);
    }

    private static void render(GuiGraphics guiGraphics, float tickDelta) {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
        if (player == null)
            return;

        RenderHealthGui.render(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight());
        RenderThirstGui.render(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight(), OverflowingBarsUtil.rightHeight(39));
        RenderTemperatureGui.render(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight());
        RenderWetnessGui.render(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight());
        RenderBodyDamageGui.render(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight());
        RenderTemperatureGui.renderColdHungerOverlay(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight());
        RenderTemperatureOverlay.render(guiGraphics, player, client.getWindow().getGuiScaledWidth(),
                client.getWindow().getGuiScaledHeight());
    }
}
