package sfiomn.legendarysurvivaloverhaul.common.integration.trinkets;

import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.items.WearableTrinketItem;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public final class TrinketsUtil {
    public static boolean isThermometerEquipped;

    private TrinketsUtil() {}

    public static ItemStack findEquippedItem(Player player, TagKey<Item> tag) {
        return LegendarySurvivalOverhaul.trinketsLoaded
                ? TrinketsIntegration.findEquippedItem(player, tag) : ItemStack.EMPTY;
    }

    public static boolean isTrinketItemEquipped(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item)
                || player.getItemInHand(InteractionHand.OFF_HAND).is(item)
                || LegendarySurvivalOverhaul.trinketsLoaded
                && TrinketsIntegration.isTrinketItemEquipped(player, item);
    }

    public static boolean isRespiratorEquipped(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD).is(ItemRegistry.RESPIRATOR.get())
                || LegendarySurvivalOverhaul.trinketsLoaded
                && TrinketsIntegration.isRespiratorEquipped(entity);
    }

    public static boolean isTrinketsItem(ItemStack stack) {
        return stack.getItem() instanceof WearableTrinketItem;
    }

    public static boolean equipTrinket(Player player, ItemStack stack) {
        return LegendarySurvivalOverhaul.trinketsLoaded
                && TrinketsIntegration.equipTrinket(player, stack);
    }
}
