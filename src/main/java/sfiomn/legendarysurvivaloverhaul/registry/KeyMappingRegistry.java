package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class KeyMappingRegistry {
    public static KeyMapping showAddedDesc;
    public static KeyMapping showBodyHealth;

    public static void register() {
        // Unbound by default: Minecraft's KeyMapping.MAP holds only one binding per physical
        // key, so defaulting this to GLFW_KEY_LEFT_SHIFT silently stole Shift's key events from
        // vanilla sneak and any other mod/custom keybind sharing that key. Players can bind it
        // themselves in Controls if they want the tooltip-expand feature.
        showAddedDesc = KeyBindingHelper.registerKeyBinding(create("added_desc", InputConstants.UNKNOWN.getValue()));
        showBodyHealth = KeyBindingHelper.registerKeyBinding(create("body_health", GLFW.GLFW_KEY_H));
    }

    /**
     * One-time fixup for players upgrading from a version that defaulted {@code added_desc} to
     * Left Shift. Minecraft persists bound keys by name in options.txt, so merely changing our
     * default does nothing for existing installs: their saved options.txt still has it on Left
     * Shift, still hijacking that key's events. Call once Minecraft's options have finished
     * loading (e.g. on the client-started lifecycle event) so we can detect and clear that saved
     * binding. Guarded by a marker file so a player who deliberately rebinds to Shift afterward
     * is left alone.
     */
    public static void migrateLegacyShiftBinding(Minecraft client) {
        Path marker = LegendarySurvivalOverhaul.modConfigPath.resolve(".added_desc_shift_unbind_migrated");
        if (Files.exists(marker))
            return;

        try {
            Files.createDirectories(marker.getParent());

            if (showAddedDesc.matches(GLFW.GLFW_KEY_LEFT_SHIFT, 0)) {
                showAddedDesc.setKey(InputConstants.UNKNOWN);
                KeyMapping.resetMapping();
                client.options.save();
                LegendarySurvivalOverhaul.LOGGER.info("Unbound the '{}' tooltip keybind from Left Shift (it was hijacking vanilla sneak and other keybinds sharing that key); rebind it in Controls if you want it.", "added_desc");
            }

            Files.createFile(marker);
        } catch (IOException e) {
            LegendarySurvivalOverhaul.LOGGER.warn("Could not write the added_desc keybind migration marker", e);
        }
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, InputConstants.Type.KEYSYM, key, "key."+ LegendarySurvivalOverhaul.MOD_ID + ".title");
    }
}
