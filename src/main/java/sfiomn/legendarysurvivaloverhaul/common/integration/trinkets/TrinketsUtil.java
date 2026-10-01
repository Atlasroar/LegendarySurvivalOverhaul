package sfiomn.legendarysurvivaloverhaul.common.integration.trinkets;

import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.common.items.WearableTrinketItem;

public final class TrinketsUtil {
    public static boolean isThermometerEquipped;

    private TrinketsUtil() {
    }

    public static boolean isTrinketItemEquipped(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item)
                || player.getItemInHand(InteractionHand.OFF_HAND).is(item)
                || TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(item))
                .orElse(false);
    }

    public static boolean isTrinketsItem(ItemStack stack) {
        return stack.getItem() instanceof WearableTrinketItem;
    }

    public static boolean equipTrinket(Player player, ItemStack stack) {
        return TrinketItem.equipItem(player, stack);
    }
}
