package sfiomn.legendarysurvivaloverhaul.common.integration.trinkets;

import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.common.items.WearableTrinketItem;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public final class TrinketsIntegration {

    private TrinketsIntegration() {
    }

    public static void register() {
        WearableTrinket wearable = new WearableTrinket();
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item instanceof WearableTrinketItem)
                TrinketsApi.registerTrinket(item, wearable);
        });
        TrinketSlotConfig.register();
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
        return TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(item))
                .orElse(false);
    }

    public static boolean isRespiratorEquipped(LivingEntity entity) {
        return entity instanceof Player player && TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(ItemRegistry.RESPIRATOR.get()))
                .orElse(false);
    }

    public static boolean equipTrinket(Player player, ItemStack stack) {
        return TrinketItem.equipItem(player, stack);
    }
}
