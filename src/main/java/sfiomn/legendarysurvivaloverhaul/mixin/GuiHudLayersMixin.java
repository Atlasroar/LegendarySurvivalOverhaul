package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.events.FabricHudCallbacks;
import sfiomn.legendarysurvivaloverhaul.client.render.OverflowingBarsHealthRenderer;
import sfiomn.legendarysurvivaloverhaul.config.Config;

@Mixin(Gui.class)
abstract class GuiHudLayersMixin {
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
            callback.cancel();
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
