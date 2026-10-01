package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.List;
import java.util.ListIterator;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @Inject(method = "getGameInformation", at = @At("RETURN"))
    private void legendarysurvivaloverhaul$hideGameCoordinates(
            CallbackInfoReturnable<List<String>> callback) {
        hideCoordinates(callback.getReturnValue());
    }

    @Inject(method = "getSystemInformation", at = @At("RETURN"))
    private void legendarysurvivaloverhaul$hideSystemCoordinates(
            CallbackInfoReturnable<List<String>> callback) {
        hideCoordinates(callback.getReturnValue());
    }

    private static void hideCoordinates(List<String> lines) {
        Minecraft client = Minecraft.getInstance();
        if (!Config.Baked.hideInfoFromDebug || client.player == null
                || client.player.isCreative() || client.player.isSpectator())
            return;

        ListIterator<String> iterator = lines.listIterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("XYZ:")) {
                iterator.set(Component.translatable(
                        "message." + LegendarySurvivalOverhaul.MOD_ID + ".warning_use_compass").getString());
            } else if (line.startsWith("Chunk:") || line.startsWith("Block:") || line.startsWith("Facing:")) {
                iterator.remove();
            } else if (line.contains("Targeted")) {
                iterator.set(line.substring(0, line.indexOf(':') + 1));
            }
        }
    }
}
