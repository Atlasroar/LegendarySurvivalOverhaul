package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sfiomn.legendarysurvivaloverhaul.registry.EnchantmentRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Set;

public final class FabricLootHooks {
    private static final Set<ResourceLocation> HEART_FRAGMENT_TABLES = Set.of(
            minecraftLootTable("chests/buried_treasure"),
            minecraftLootTable("chests/jungle_temple"),
            minecraftLootTable("chests/abandoned_mineshaft"),
            minecraftLootTable("chests/bastion_treasure")
    );
    private static final Set<ResourceLocation> PURITY_BOOK_TABLES = Set.of(
            minecraftLootTable("chests/bastion_treasure"),
            minecraftLootTable("chests/bastion_bridge"),
            minecraftLootTable("chests/bastion_hoglin_stable"),
            minecraftLootTable("chests/bastion_other"),
            minecraftLootTable("chests/nether_bridge")
    );

    private FabricLootHooks() {
    }

    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (HEART_FRAGMENT_TABLES.contains(id))
                tableBuilder.withPool(heartFragmentPool());

            if (id.equals(minecraftLootTable("chests/bastion_treasure")))
                addWeightedChance(tableBuilder, ItemRegistry.COLD_RESISTANCE_RING.get(), 5, 95);

            if (PURITY_BOOK_TABLES.contains(id))
                tableBuilder.withPool(purityBookPool());

            if (id.equals(minecraftLootTable("chests/desert_pyramid")))
                addWeightedChance(tableBuilder, ItemRegistry.HEAT_RESISTANCE_RING.get(), 5, 95);

            if (id.equals(minecraftLootTable("chests/pillager_outpost")))
                addWeightedChance(tableBuilder, ItemRegistry.FIRST_AID_SUPPLIES.get(), 1, 99);

            if (id.equals(minecraftLootTable("entities/drowned")))
                addWeightedChance(tableBuilder, ItemRegistry.WATER_PURIFIER.get(), 1, 100);

            if (id.equals(minecraftLootTable("gameplay/fishing/treasure")))
                addWeightedChance(tableBuilder, ItemRegistry.SPONGE.get(), 20, 80);

            if (id.equals(minecraftLootTable("gameplay/piglin_bartering")))
                addWeightedChance(tableBuilder, ItemRegistry.NETHER_CHALICE.get(), 1, 99);

            injectLootPool(tableBuilder, id, "chests/buried_treasure", "soulfire_bottle_buried");
            injectLootPool(tableBuilder, id, "chests/shipwreck_treasure", "soulfire_bottle_shipwreck");
            injectLootPool(tableBuilder, id, "chests/underwater_ruin_big", "soulfire_bottle_big_ruin");
            injectLootPool(tableBuilder, id, "chests/underwater_ruin_small", "soulfire_bottle_small_ruin");
            injectLootPool(tableBuilder, id, "chests/simple_dungeon", "safety_lantern_dungeon");
            injectLootPool(tableBuilder, id, "chests/abandoned_mineshaft", "safety_lantern_mineshaft");
            injectLootPool(tableBuilder, id, "chests/stronghold_corridor", "safety_lantern_stronghold");
        });
    }

    private static void injectLootPool(LootTable.Builder tableBuilder, ResourceLocation id, String sourceTable, String injectedTable) {
        if (id.equals(minecraftLootTable(sourceTable))) {
            tableBuilder.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                    .add(LootTableReference.lootTableReference(
                            new ResourceLocation("legendarysurvivaloverhaul", injectedTable))));
        }
    }

    private static LootPool.Builder heartFragmentPool() {
        return oneRoll()
                .add(LootItem.lootTableItem(ItemRegistry.HEART_FRAGMENT.get()).setWeight(30))
                .add(LootItem.lootTableItem(ItemRegistry.HEART_FRAGMENT.get()).setWeight(5)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))
                .add(EmptyLootItem.emptyItem().setWeight(80));
    }

    private static LootPool.Builder purityBookPool() {
        return oneRoll()
                .add(LootItem.lootTableItem(Items.ENCHANTED_BOOK).setWeight(5)
                        .apply(new SetEnchantmentsFunction.Builder(false)
                                .withEnchantment(EnchantmentRegistry.PURITY.get(), ConstantValue.exactly(1))))
                .add(EmptyLootItem.emptyItem().setWeight(95));
    }

    private static void addWeightedChance(LootTable.Builder table, Item item, int itemWeight, int emptyWeight) {
        table.withPool(oneRoll()
                .add(LootItem.lootTableItem(item).setWeight(itemWeight))
                .add(EmptyLootItem.emptyItem().setWeight(emptyWeight)));
    }

    private static LootPool.Builder oneRoll() {
        return LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 1.0F));
    }

    private static ResourceLocation minecraftLootTable(String path) {
        return new ResourceLocation("minecraft", path);
    }
}
