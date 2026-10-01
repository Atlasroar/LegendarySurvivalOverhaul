package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.events.FabricHudCallbacks;
import sfiomn.legendarysurvivaloverhaul.client.render.OverflowingBarsHealthRenderer;
import sfiomn.legendarysurvivaloverhaul.client.render.RenderHealthGui;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@Mixin(Gui.class)
abstract class GuiHudLayersMixin {
    @ModifyArg(method = "renderPlayerHealth",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    ordinal = 0),
            index = 2)
    private int legendarysurvivaloverhaul$raiseArmorRowFirst(int y) {
        return legendarysurvivaloverhaul$raiseArmorRow(y);
    }

    @ModifyArg(method = "renderPlayerHealth",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    ordinal = 1),
            index = 2)
    private int legendarysurvivaloverhaul$raiseArmorRowSecond(int y) {
        return legendarysurvivaloverhaul$raiseArmorRow(y);
    }

    @ModifyArg(method = "renderPlayerHealth",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
                    ordinal = 2),
            index = 2)
    private int legendarysurvivaloverhaul$raiseArmorRowThird(int y) {
        return legendarysurvivaloverhaul$raiseArmorRow(y);
    }

    private static int legendarysurvivaloverhaul$raiseArmorRow(int y) {
        Player player = Minecraft.getInstance().player;
        return player == null ? y : y - RenderHealthGui.additionalHeartRows(player) * 10;
    }

    @Inject(method = "renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V",
            at = @At("HEAD"), cancellable = true)
    private void legendarysurvivaloverhaul$renderOverflowingHearts(GuiGraphics guiGraphics, Player player,
                                                                    int x, int y, int rowHeight, int regenerationOffset,
                                                                    float maxHealth, int health, int displayHealth,
                                                                    int absorption, boolean blink,
                                                                    CallbackInfo callback) {
        if (Config.Baked.healthOverhaulEnabled && !LegendarySurvivalOverhaul.overflowingbarsLoaded) {
            OverflowingBarsHealthRenderer.INSTANCE.renderPlayerHealth(guiGraphics, x, y, player,
                    Minecraft.getInstance().getProfiler());
            RenderHealthGui.renderBrokenHearts(guiGraphics, player, x, y, rowHeight);
            callback.cancel();
        }
    }

    @Inject(method = "renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V",
            at = @At("TAIL"))
    private void legendarysurvivaloverhaul$renderBrokenHeartReplacements(GuiGraphics guiGraphics, Player player,
                                                                          int x, int y, int rowHeight,
                                                                          int regenerationOffset, float maxHealth,
                                                                          int health, int displayHealth,
                                                                          int absorption, boolean blink,
                                                                          CallbackInfo callback) {
        if (Config.Baked.healthOverhaulEnabled && LegendarySurvivalOverhaul.overflowingbarsLoaded) {
            RenderHealthGui.renderBrokenHearts(guiGraphics, player, x, y, rowHeight);
        }
    }

    @Inject(method = "render",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderPlayerHealth(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    shift = At.Shift.AFTER))
    private void legendarysurvivaloverhaul$renderStatusBarLayers(GuiGraphics guiGraphics, float partialTick,
                                                                  CallbackInfo callback) {
        FabricHudCallbacks.renderAfterStatusBars(guiGraphics);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void legendarysurvivaloverhaul$renderScreenLayers(GuiGraphics guiGraphics, float partialTick,
                                                               CallbackInfo callback) {
        FabricHudCallbacks.renderAfterGui(guiGraphics);
    }
}
