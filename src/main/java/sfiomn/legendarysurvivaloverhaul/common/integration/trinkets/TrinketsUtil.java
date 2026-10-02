package sfiomn.legendarysurvivaloverhaul.common.integration.trinkets;

import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.common.items.WearableTrinketItem;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public final class TrinketsUtil {
    public static boolean isThermometerEquipped;

    private TrinketsUtil() {
    }

    /**
     * Finds the first equipped trinket item matching the given tag, if any. Used by the air quality system to
     * find an equipped Respirator regardless of which trinket slot it was placed in.
     */
    public static ItemStack findEquippedItem(Player player, TagKey<Item> tagKey) {
        return TrinketsApi.getTrinketComponent(player)
                .flatMap(component -> component.getEquipped(itemStack -> itemStack.is(tagKey))
                        .stream()
                        .findFirst()
                        .map(Tuple::getB))
                .orElse(ItemStack.EMPTY);
    }

    public static boolean isTrinketItemEquipped(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item)
                || player.getItemInHand(InteractionHand.OFF_HAND).is(item)
                || TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(item))
                .orElse(false);
    }

    public static boolean isRespiratorEquipped(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(ItemRegistry.RESPIRATOR.get())
                || entity instanceof Player player && TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(ItemRegistry.RESPIRATOR.get()))
                .orElse(false);
    }

    public static boolean isTrinketsItem(ItemStack stack) {
        return stack.getItem() instanceof WearableTrinketItem;
    }

    public static boolean equipTrinket(Player player, ItemStack stack) {
        return TrinketItem.equipItem(player, stack);
    }
}
