import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.entity.EquipmentSlot;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.WearableTrinketItem;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantmentRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.List;
import java.util.UUID;

public final class OptionalIntegrationsCheck implements ModInitializer {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                String expectedInterval = System.getenv("LSO_CHECK_SYNC_INTERVAL");
                if (expectedInterval != null)
                    check(Config.Baked.routinePacketSync == Integer.parseInt(expectedInterval),
                            "Saved non-default TOML setting survives optional editor installation/removal");
                List<Enchantment> enchantments = List.of(EnchantmentRegistry.REFRESHING.get(),
                        EnchantmentRegistry.PURITY.get(), EnchantmentRegistry.RESERVOIR.get());
                for (Enchantment enchantment : enchantments) {
                    check(enchantment.canEnchant(new ItemStack(ItemRegistry.CANTEEN.get())), "Accept regular canteen");
                    check(enchantment.canEnchant(new ItemStack(ItemRegistry.LARGE_CANTEEN.get())), "Accept large canteen");
                    for (var item : List.of(Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_CHESTPLATE,
                            Items.BOW, Items.SHIELD, Items.POTION, ItemRegistry.WATER_PURIFIER.get()))
                        check(!enchantment.canEnchant(new ItemStack(item)), "Reject non-canteen " + item);
                    check(enchantment.isTreasureOnly(), "No enchanting-table acquisition");
                    check(!enchantment.isTradeable(), "No villager trading");
                    check(enchantment.isDiscoverable(), "Retain random loot eligibility");
                    for (int level = 1; level <= 50; level++) {
                        check(EnchantmentHelper.getAvailableEnchantmentResults(level, new ItemStack(Items.BOOK), false)
                                .stream().noneMatch(entry -> entry.enchantment == enchantment),
                                "Never offered by enchanting-table selection");
                        check(EnchantmentHelper.getAvailableEnchantmentResults(level, new ItemStack(Items.DIAMOND_SWORD), true)
                                .stream().noneMatch(entry -> entry.enchantment == enchantment),
                                "Never selected on a looted weapon");
                    }
                    check(EnchantmentHelper.getAvailableEnchantmentResults(30, new ItemStack(Items.BOOK), true)
                            .stream().map(entry -> entry.enchantment).anyMatch(entry -> entry == enchantment),
                            "Available to treasure-book loot selection");
                }

                ServerPlayer player = new ServerPlayer(server, server.overworld(),
                        new GameProfile(UUID.randomUUID(), "OptionalCheck"));
                for (Enchantment enchantment : enchantments) {
                    ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, 1));
                    AnvilMenu anvil = new AnvilMenu(0, player.getInventory());
                    anvil.getSlot(0).set(new ItemStack(Items.DIAMOND_SWORD));
                    anvil.getSlot(1).set(book.copy());
                    anvil.createResult();
                    check(anvil.getSlot(2).getItem().isEmpty(), "Anvil rejects canteen book on a weapon");
                    anvil.getSlot(0).set(new ItemStack(ItemRegistry.CANTEEN.get()));
                    anvil.createResult();
                    check(EnchantmentHelper.getItemEnchantmentLevel(enchantment, anvil.getSlot(2).getItem()) == 1,
                            "Anvil accepts canteen book on a canteen");
                }
                check(ItemRegistry.THERMOMETER.get() instanceof WearableTrinketItem, "Thermometer registered");
                player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemRegistry.THERMOMETER.get()));
                check(TrinketsUtil.isTrinketItemEquipped(player, ItemRegistry.THERMOMETER.get()),
                        "Held thermometer works with or without Trinkets");
                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.RESPIRATOR.get()));
                check(TrinketsUtil.isRespiratorEquipped(player), "Head-slot Respirator works without accessory API");
                player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                check(!TrinketsUtil.isRespiratorEquipped(player), "No phantom equipped Respirator");
                if (!LegendarySurvivalOverhaul.trinketsLoaded) {
                    check(!TrinketsUtil.equipTrinket(player, new ItemStack(ItemRegistry.SPONGE.get())),
                            "No accessory slots without Trinkets");
                } else
                    TrinketsAssertions.checkRegisteredAccessories();

                ShapedRecipe ring = (ShapedRecipe) server.getRecipeManager()
                        .byKey(new ResourceLocation("legendarysurvivaloverhaul", "thermal_resistance_ring")).orElseThrow();
                Ingredient potion = ring.getIngredients().get(4);
                check(!potion.test(new ItemStack(Items.POTION)), "Ring does not accept an empty potion");
                check(!potion.test(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)),
                        "Ring does not accept a water bottle");
                check(potion.test(PotionUtils.setPotion(new ItemStack(Items.POTION),
                        MobEffectRegistry.TEMPERATURE_IMMUNITY_POTION.get())),
                        "Ring accepts its required immunity potion");
                for (String recipe : List.of("thermometer", "healing_herbs", "morphine", "soulfire_bottle_from_glass"))
                    check(server.getRecipeManager().byKey(new ResourceLocation("legendarysurvivaloverhaul", recipe)).isPresent(),
                            "Recipe still loads after Forge data removal: " + recipe);
                check(server.getRecipeManager().byKey(new ResourceLocation("legendarysurvivaloverhaul", "thermometer"))
                        .orElseThrow().getIngredients().get(0).getItems().length > 0,
                        "Thermometer glass ingredient is populated");
                for (String consumable : List.of("apple_cider", "chicken_soup", "fruit_salad", "hot_cocoa",
                        "melon_juice", "melon_popsicle", "milk_bottle", "pumpkin_soup", "tomato",
                        "tomato_sauce", "vegetable_soup"))
                    check(ThirstDataManager.getConsumable(new ResourceLocation("farmersdelight", consumable)) != null,
                            "Refabricated hydration entry loads: " + consumable);
                check(TemperatureDataManager.getBlock(new ResourceLocation("farmersdelight", "stove")).size() == 2,
                        "Refabricated lit/unlit stove temperature data loads");

                System.out.println("OPTIONAL_INTEGRATIONS_CHECK_PASSED trinkets="
                        + FabricLoader.getInstance().isModLoaded("trinkets")
                        + " fzzy=" + FabricLoader.getInstance().isModLoaded("fzzy_config"));
            } finally {
                server.halt(false);
            }
        });
    }
}
