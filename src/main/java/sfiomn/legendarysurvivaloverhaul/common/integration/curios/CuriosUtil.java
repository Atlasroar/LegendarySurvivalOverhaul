package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CuriosUtil {
    public static boolean isThermometerEquipped;

    private CuriosUtil() {
    }

    public static boolean isCurioItemEquipped(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item)
                || player.getItemInHand(InteractionHand.OFF_HAND).is(item);
    }

    public static boolean isCuriosItem(ItemStack stack) {
        return false;
    }

    public static boolean equipCurio(Player player, ItemStack stack, InteractionHand hand) {
        return false;
    }
}
