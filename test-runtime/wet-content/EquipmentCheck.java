package lso.check;

import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricEquipmentAttributeHooks;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;
import sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EquipmentCheck {
    private static boolean recording;
    private static int calculations;
    private static int dynamicCalls;
    private static float dynamicResistance;
    private static final boolean DYNAMIC =
            FabricLoader.getInstance().isModLoaded("lso_dynamic_equipment_check");

    public static void register() {
        if (DYNAMIC) {
            Registry.register(TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY,
                    new ResourceLocation("lso_wet_content_check", "dynamic_equipment"),
                    new AttributeModifierBase() {
                        @Override
                        public JsonTemperatureResistance getItemAttributes(ItemStack stack) {
                            if (recording)
                                dynamicCalls++;
                            return new JsonTemperatureResistance(0, dynamicResistance, 0, 0);
                        }
                    });
        }
    }

    public static void recordCalculation() {
        if (recording)
            calculations++;
    }

    private static int update(ServerPlayer player) {
        calculations = dynamicCalls = 0;
        recording = true;
        try {
            FabricEquipmentAttributeHooks.updatePlayerEquipmentModifiers(player);
        } finally {
            recording = false;
        }
        return calculations;
    }

    public static void run(ServerLevel level) {
        ServerPlayer player = new ServerPlayer(level.getServer(), level,
                new GameProfile(UUID.randomUUID(), "EquipmentCheck"));
        player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK));
        player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.STICK));
        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.SNOW_HELMET.get()));
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.SNOW_CHEST.get()));
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistry.SNOW_LEGGINGS.get()));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.SNOW_BOOTS.get()));
        Map<ResourceLocation, JsonTemperatureResistance> temperatures = new HashMap<>();
        Map<ResourceLocation, JsonBodyPartResistance> bodies = new HashMap<>();
        for (var item : BuiltInRegistries.ITEM) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            var temperature = TemperatureDataManager.getItem(id);
            var body = BodyDamageDataManager.getBodyResistanceItem(id);
            if (temperature != null)
                temperatures.put(id, temperature);
            if (body != null)
                bodies.put(id, body);
        }
        boolean temperatureEnabled = Config.Baked.temperatureEnabled;
        boolean bodyEnabled = Config.Baked.localizedBodyDamageEnabled;
        double heatingCoat = Config.TEMPERATURE.heatingCoat1Modifier.get();
        var originalManager = TemperatureDataManager.internalItem;
        ResourceLocation bootsId = BuiltInRegistries.ITEM.getKey(ItemRegistry.SNOW_BOOTS.get());
        try {
            Config.Baked.temperatureEnabled = Config.Baked.localizedBodyDamageEnabled = true;
            require(update(player) == 12, "Initial calculation covers all six slots");
            assertValues(player);
            int unchanged = 0;
            for (int i = 0; i < 20; i++)
                unchanged += update(player);
            require(unchanged == (DYNAMIC ? 240 : 0), "Unchanged equipment calculation count");
            System.out.println("EQUIPMENT_UNCHANGED_CALCULATIONS: 240 -> " + unchanged);
            assertValues(player);
            if (!DYNAMIC)
                benchmark(player);

            player.getMainHandItem().setCount(2);
            require(update(player) == (DYNAMIC ? 12 : 2), "In-place count change");
            assertValues(player);
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            boots.setDamageValue(1);
            boots.getOrCreateTag().putString("ArmorPadding", "heating1");
            boots.enchant(Enchantments.UNBREAKING, 1);
            require(update(player) == (DYNAMIC ? 12 : 2), "Damage, coat and enchantment changes");
            require(!boots.getOrCreateTag().contains("ArmorPadding"), "Legacy coat migration still runs");
            assertValues(player);
            require(update(player) == (DYNAMIC ? 12 : 0), "Migrated stack snapshot remains reusable");
            player.setItemSlot(EquipmentSlot.FEET, boots.copy());
            require(update(player) == (DYNAMIC ? 12 : 0), "Equal replacement stack reuses values");
            player.getItemBySlot(EquipmentSlot.FEET).enchant(Enchantments.MENDING, 1);
            require(update(player) == (DYNAMIC ? 12 : 2), "Replacement stack NBT is detected");
            assertValues(player);
            if (!DYNAMIC)
                benchmark(player);

            Config.TEMPERATURE.heatingCoat1Modifier.validateAndSet(heatingCoat + 1);
            Config.bake(Config.TEMPERATURE);
            require(update(player) == (DYNAMIC ? 12 : 2), "Config bake changes coat strength immediately");
            assertValues(player);

            var changed = new JsonTemperatureResistance(-2, 3, 4, 5);
            TemperatureItemListener.acceptServerTemperatureItems(Map.of(bootsId, changed));
            update(player);
            assertValues(player);
            changed.coldResistance = 7;
            require(update(player) == (DYNAMIC ? 12 : 2), "In-place resistance-data mutation");
            assertValues(player);
            TemperatureItemListener.acceptServerTemperatureItems(Map.of());
            update(player);
            assertValues(player);
            TemperatureItemListener.acceptServerTemperatureItems(temperatures);
            update(player);
            assertValues(player);

            var changedBody = new JsonBodyPartResistance(1, 2, 3, 4, 5, 6, 7);
            BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(Map.of(bootsId, changedBody));
            update(player);
            assertValues(player);
            changedBody.feetResistance = 9;
            update(player);
            assertValues(player);
            BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(Map.of());
            update(player);
            assertValues(player);

            UUID uuid = TemperatureUtilInternal.equipmentSlotTemperatureUuid.get(EquipmentSlot.FEET);
            var cold = TemperatureUtilInternal.COLD_RESISTANCE.getAttribute(player);
            cold.removeModifier(uuid);
            require(update(player) == (DYNAMIC ? 12 : 0), "Repair does not require recalculation");
            require(cold.getModifier(uuid) != null, "Missing cached modifier is repaired");
            TemperatureUtilInternal.COLD_RESISTANCE.addModifier(player, uuid, 999);
            update(player);
            assertValues(player);

            Config.Baked.localizedBodyDamageEnabled = false;
            update(player);
            assertValues(player);
            Config.Baked.temperatureEnabled = false;
            require(update(player) == 0 && dynamicCalls == 0, "Disable clears without calculating");
            assertValues(player);
            Config.Baked.temperatureEnabled = Config.Baked.localizedBodyDamageEnabled = true;
            update(player);
            assertValues(player);
            for (EquipmentSlot slot : EquipmentSlot.values())
                player.setItemSlot(slot, ItemStack.EMPTY);
            require(update(player) == 0, "Unequip clears without calculating");
            assertValues(player);
            player.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.SNOW_BOOTS.get()));

            TemperatureDataManager.internalItem = id -> id.equals(bootsId)
                    ? new JsonTemperatureResistance(0, dynamicResistance, 0, 0) : originalManager.get(id);
            dynamicResistance = 11;
            require(update(player) == 2, "Replacement manager retains uncached calculations");
            assertValues(player);
            dynamicResistance = 13;
            require(update(player) == 2, "Replacement manager refreshes unchanged gear");
            assertValues(player);
            TemperatureDataManager.internalItem = originalManager;
            update(player);
            assertValues(player);
            if (DYNAMIC) {
                dynamicResistance = 17;
                update(player);
                require(dynamicCalls == 1, "Custom modifier runs for unchanged gear");
                assertValues(player);
                dynamicResistance = 19;
                update(player);
                require(dynamicCalls == 1, "Custom modifier remains dynamic");
                assertValues(player);
                System.out.println("EQUIPMENT_DYNAMIC_CHECK_PASSED");
            }
            System.out.println("EQUIPMENT_CHECK_PASSED");
        } finally {
            TemperatureDataManager.internalItem = originalManager;
            TemperatureItemListener.acceptServerTemperatureItems(temperatures);
            BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(bodies);
            Config.TEMPERATURE.heatingCoat1Modifier.validateAndSet(heatingCoat);
            Config.bake(Config.TEMPERATURE);
            Config.Baked.temperatureEnabled = temperatureEnabled;
            Config.Baked.localizedBodyDamageEnabled = bodyEnabled;
            dynamicResistance = 0;
        }
    }

    private static void assertValues(ServerPlayer player) {
        evaluate(player, false);
    }

    private static void benchmark(ServerPlayer player) {
        for (int i = 0; i < 2000; i++) {
            evaluate(player, true);
            FabricEquipmentAttributeHooks.updatePlayerEquipmentModifiers(player);
        }
        long reference = 0;
        long optimized = 0;
        for (int round = 0; round < 4; round++) {
            for (int phase = 0; phase < 2; phase++) {
                boolean original = (round + phase) % 2 == 0;
                long start = System.nanoTime();
                for (int i = 0; i < 10000; i++) {
                    if (original)
                        evaluate(player, true);
                    else
                        FabricEquipmentAttributeHooks.updatePlayerEquipmentModifiers(player);
                }
                if (original)
                    reference += System.nanoTime() - start;
                else
                    optimized += System.nanoTime() - start;
            }
        }
        System.out.printf("EQUIPMENT_UPDATE_TIMING: reference_ms=%.2f optimized_ms=%.2f%n",
                reference / 1000000.0, optimized / 1000000.0);
        assertValues(player);
    }

    private static void evaluate(ServerPlayer player, boolean reconcile) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            boolean supported = !stack.isEmpty() && ItemUtil.canBeEquippedInSlot(stack, slot);
            JsonTemperatureResistance temperature = new JsonTemperatureResistance();
            if (Config.Baked.temperatureEnabled && supported) {
                for (var modifier : TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY)
                    temperature.add(modifier.getItemAttributes(stack));
            }
            UUID temperatureUuid = TemperatureUtilInternal.equipmentSlotTemperatureUuid.get(slot);
            assertModifier(player, TemperatureUtilInternal.HEATING_TEMPERATURE, temperatureUuid,
                    Math.max(temperature.temperature, 0), reconcile);
            assertModifier(player, TemperatureUtilInternal.COOLING_TEMPERATURE, temperatureUuid,
                    Math.min(temperature.temperature, 0), reconcile);
            assertModifier(player, TemperatureUtilInternal.HEAT_RESISTANCE, temperatureUuid, temperature.heatResistance, reconcile);
            assertModifier(player, TemperatureUtilInternal.COLD_RESISTANCE, temperatureUuid, temperature.coldResistance, reconcile);
            assertModifier(player, TemperatureUtilInternal.THERMAL_RESISTANCE, temperatureUuid, temperature.thermalResistance, reconcile);
            var body = Config.Baked.localizedBodyDamageEnabled && supported
                    ? BodyDamageDataManager.getBodyResistanceItem(BuiltInRegistries.ITEM.getKey(stack.getItem())) : null;
            UUID bodyUuid = BodyDamageUtilInternal.equipmentSlotBodyResistanceUuid.get(slot);
            assertModifier(player, BodyDamageUtilInternal.BODY_RESISTANCE, bodyUuid, body == null ? 0 : body.bodyResistance, reconcile);
            assertModifier(player, BodyDamageUtilInternal.HEAD_RESISTANCE, bodyUuid, body == null ? 0 : body.headResistance, reconcile);
            assertModifier(player, BodyDamageUtilInternal.CHEST_RESISTANCE, bodyUuid, body == null ? 0 : body.chestResistance, reconcile);
            assertModifier(player, BodyDamageUtilInternal.RIGHT_ARM_RESISTANCE, bodyUuid, body == null ? 0 : body.rightArmResistance, reconcile);
            assertModifier(player, BodyDamageUtilInternal.LEFT_ARM_RESISTANCE, bodyUuid, body == null ? 0 : body.leftArmResistance, reconcile);
            assertModifier(player, BodyDamageUtilInternal.LEGS_RESISTANCE, bodyUuid, body == null ? 0 : body.legsResistance, reconcile);
            assertModifier(player, BodyDamageUtilInternal.FEET_RESISTANCE, bodyUuid, body == null ? 0 : body.feetResistance, reconcile);
        }
    }

    private static void assertModifier(ServerPlayer player, AttributeBuilder builder, UUID uuid,
                                       double expected, boolean reconcile) {
        var attribute = builder.getAttribute(player);
        var modifier = attribute.getModifier(uuid);
        if (reconcile) {
            if (expected == 0) {
                if (modifier != null)
                    attribute.removeModifier(uuid);
            } else if (modifier == null || Double.compare(modifier.getAmount(), expected) != 0) {
                builder.addModifier(player, uuid, expected);
            }
            return;
        }
        require(expected == 0 ? modifier == null
                : modifier != null && Math.abs(modifier.getAmount() - expected) < 0.0001,
                "Equipment modifier parity: " + uuid + ", expected " + expected);
    }

    private static void require(boolean condition, String message) {
        if (!condition)
            throw new AssertionError(message);
    }
}
