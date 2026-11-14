package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.item.CoatEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;
import sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class FabricEquipmentAttributeHooks {
    private static final EquipmentSlot[] SLOTS = EquipmentSlot.values();
    private static final Map<Player, CachedTemperature[]> TEMPERATURE_CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private FabricEquipmentAttributeHooks() {
    }

    public static void updatePlayerEquipmentModifiers(Player player) {
        CachedTemperature[] cache = null;
        if (canCacheTemperature()) {
            cache = TEMPERATURE_CACHE.computeIfAbsent(player, ignored -> new CachedTemperature[SLOTS.length]);
        } else {
            TEMPERATURE_CACHE.remove(player);
        }
        for (EquipmentSlot slot : SLOTS) {
            updateSlotModifiers(player, slot, player.getItemBySlot(slot), cache);
        }
    }

    private static boolean canCacheTemperature() {
        // Extensions can depend on time, tags or external state rather than just the stack/data.
        if (TemperatureDataManager.internalItem == null
                || TemperatureDataManager.internalItem.getClass() != TemperatureItemListener.class
                || TemperatureUtil.internal == null
                || TemperatureUtil.internal.getClass() != TemperatureUtilInternal.class
                || TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY.size() != 2)
            return false;

        boolean item = false;
        boolean coat = false;
        for (AttributeModifierBase modifier : TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY) {
            if (modifier.getClass() == ItemAttributeModifier.class)
                item = true;
            else if (modifier.getClass() == CoatAttributeModifier.class)
                coat = true;
            else
                return false;
        }
        return item && coat;
    }

    private static JsonTemperatureResistance getTemperature(EquipmentSlot slot, ItemStack stack,
                                                            ResourceLocation itemId, CachedTemperature[] cache) {
        if (!Config.Baked.temperatureEnabled || itemId == null) {
            if (cache != null)
                cache[slot.ordinal()] = null;
            return null;
        }

        JsonTemperatureResistance itemValues = cache == null ? null : TemperatureDataManager.getItem(itemId);
        CachedTemperature cached = cache == null ? null : cache[slot.ordinal()];
        if (cached != null && cached.matches(stack, itemValues))
            return cached.temperature;

        JsonTemperatureResistance temperature = new JsonTemperatureResistance();
        for (AttributeModifierBase modifier : TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY) {
            temperature.add(modifier.getItemAttributes(stack));
        }
        if (cache != null) {
            CoatEnum coat = CoatEnum.getFromId(TemperatureUtil.getArmorCoatTag(stack));
            JsonTemperatureResistance snapshot = itemValues == null ? null : new JsonTemperatureResistance(
                    itemValues.temperature, itemValues.heatResistance, itemValues.coldResistance,
                    itemValues.thermalResistance);
            // Copy after calculation, since reading a legacy coat tag migrates the stack's NBT.
            cache[slot.ordinal()] = new CachedTemperature(stack.copy(), snapshot, coat,
                    coat == null ? 0 : coat.modifier(), temperature);
        }
        return temperature;
    }

    private record CachedTemperature(ItemStack stack, JsonTemperatureResistance itemValues,
                                     CoatEnum coat, double coatModifier, JsonTemperatureResistance temperature) {
        private boolean matches(ItemStack current, JsonTemperatureResistance values) {
            return ItemStack.matches(stack, current)
                    && Double.compare(coatModifier, coat == null ? 0 : coat.modifier()) == 0
                    && (itemValues == null ? values == null : values != null
                    && Float.compare(itemValues.temperature, values.temperature) == 0
                    && Float.compare(itemValues.heatResistance, values.heatResistance) == 0
                    && Float.compare(itemValues.coldResistance, values.coldResistance) == 0
                    && Float.compare(itemValues.thermalResistance, values.thermalResistance) == 0);
        }
    }

    private static void updateSlotModifiers(Player player, EquipmentSlot slot, ItemStack stack,
                                            CachedTemperature[] cache) {
        UUID temperatureUuid = TemperatureUtilInternal.equipmentSlotTemperatureUuid.get(slot);
        UUID bodyResistanceUuid = BodyDamageUtilInternal.equipmentSlotBodyResistanceUuid.get(slot);
        boolean supportedStack = !stack.isEmpty() && ItemUtil.canBeEquippedInSlot(stack, slot);
        ResourceLocation itemId = supportedStack ? BuiltInRegistries.ITEM.getKey(stack.getItem()) : null;

        JsonTemperatureResistance temperature = getTemperature(slot, stack, itemId, cache);
        setModifier(player, TemperatureUtilInternal.HEATING_TEMPERATURE, temperatureUuid,
                temperature == null ? 0 : Math.max(temperature.temperature, 0));
        setModifier(player, TemperatureUtilInternal.COOLING_TEMPERATURE, temperatureUuid,
                temperature == null ? 0 : Math.min(temperature.temperature, 0));
        setModifier(player, TemperatureUtilInternal.HEAT_RESISTANCE, temperatureUuid,
                temperature == null ? 0 : temperature.heatResistance);
        setModifier(player, TemperatureUtilInternal.COLD_RESISTANCE, temperatureUuid,
                temperature == null ? 0 : temperature.coldResistance);
        setModifier(player, TemperatureUtilInternal.THERMAL_RESISTANCE, temperatureUuid,
                temperature == null ? 0 : temperature.thermalResistance);

        JsonBodyPartResistance bodyResistance = Config.Baked.localizedBodyDamageEnabled && itemId != null
                ? BodyDamageDataManager.getBodyResistanceItem(itemId) : null;
        setModifier(player, BodyDamageUtilInternal.BODY_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.bodyResistance);
        setModifier(player, BodyDamageUtilInternal.HEAD_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.headResistance);
        setModifier(player, BodyDamageUtilInternal.CHEST_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.chestResistance);
        setModifier(player, BodyDamageUtilInternal.RIGHT_ARM_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.rightArmResistance);
        setModifier(player, BodyDamageUtilInternal.LEFT_ARM_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.leftArmResistance);
        setModifier(player, BodyDamageUtilInternal.LEGS_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.legsResistance);
        setModifier(player, BodyDamageUtilInternal.FEET_RESISTANCE, bodyResistanceUuid,
                bodyResistance == null ? 0 : bodyResistance.feetResistance);
    }

    private static void setModifier(Player player, AttributeBuilder attributeBuilder, UUID uuid, double value) {
        AttributeInstance attribute = attributeBuilder.getAttribute(player);
        if (attribute == null)
            return;

        AttributeModifier existing = attribute.getModifier(uuid);
        if (value == 0) {
            if (existing != null)
                attribute.removeModifier(uuid);
        } else if (existing == null || Double.compare(existing.getAmount(), value) != 0) {
            attributeBuilder.addModifier(player, uuid, value);
        }
    }
}
