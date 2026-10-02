package sfiomn.legendarysurvivaloverhaul.common.integration.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Set;
import java.util.HashSet;

public final class TrinketSlotConfig {
    private static volatile Set<String> availablePlayerSlots;

    private TrinketSlotConfig() {}

    public static void register() {
        ResourceLocation tagId = new ResourceLocation("trinkets", "tag");
        var original = TrinketsApi.getTrinketPredicate(tagId)
                .orElseThrow(() -> new IllegalStateException("Trinkets tag predicate is not registered"));
        // Wrap only the tag check: retain the slot's other predicates and the item's canEquip checks.
        TrinketsApi.registerTrinketPredicate(tagId, (stack, slot, entity) -> {
            Set<String> allowed = configuredSlots(stack);
            return allowed == null ? original.apply(stack, slot, entity)
                    : TriState.of(allowed.contains(slotId(slot)));
        });
        ServerLifecycleEvents.SERVER_STARTED.register(TrinketSlotConfig::refreshAvailableSlots);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
            if (success) refreshAvailableSlots(server);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> availablePlayerSlots = null);
    }

    private static void refreshAvailableSlots(MinecraftServer server) {
        Set<String> slots = new HashSet<>();
        TrinketsApi.getPlayerSlots(server.overworld()).forEach((group, data) ->
                data.getSlots().keySet().forEach(slot -> slots.add(group + "/" + slot)));
        availablePlayerSlots = Set.copyOf(slots);
        warnUnavailableSlots();
    }

    public static void warnUnavailableSlots() {
        Set<String> available = availablePlayerSlots;
        if (available == null) return;
        Config.Baked.trinketSlots.forEach((item, slots) -> slots.forEach(slot -> {
            if (!available.contains(slot))
                LegendarySurvivalOverhaul.LOGGER.warn("Configured Trinkets slot {} for {} is not defined for players; this config does not create slots",
                        slot, item);
        }));
    }

    public static boolean canEquip(ItemStack stack, SlotReference slot) {
        Set<String> allowed = configuredSlots(stack);
        return allowed == null || allowed.contains(slotId(slot));
    }

    private static String slotId(SlotReference slot) {
        var type = slot.inventory().getSlotType();
        return type.getGroup() + "/" + type.getName();
    }

    private static Set<String> configuredSlots(ItemStack stack) {
        return Config.Baked.trinketSlots.get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
    }
}
