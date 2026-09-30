package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class KeyMappingRegistry {
    public static KeyMapping showAddedDesc;
    public static KeyMapping showBodyHealth;

    public static void register() {
        showAddedDesc = KeyBindingHelper.registerKeyBinding(create("added_desc", GLFW.GLFW_KEY_LEFT_SHIFT));
        showBodyHealth = KeyBindingHelper.registerKeyBinding(create("body_health", GLFW.GLFW_KEY_H));
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + LegendarySurvivalOverhaul.MOD_ID + "." + name, InputConstants.Type.KEYSYM, key, "key."+ LegendarySurvivalOverhaul.MOD_ID + ".title");
    }
}
