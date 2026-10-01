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
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import sfiomn.legendarysurvivaloverhaul.util.ItemUtil;
import sfiomn.legendarysurvivaloverhaul.util.internal.BodyDamageUtilInternal;
import sfiomn.legendarysurvivaloverhaul.util.internal.TemperatureUtilInternal;

import java.util.UUID;

public final class FabricEquipmentAttributeHooks {
    private FabricEquipmentAttributeHooks() {
    }

    public static void updatePlayerEquipmentModifiers(Player player) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND
                    || slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST
                    || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                updateSlotModifiers(player, slot, player.getItemBySlot(slot));
            }
        }
    }

    private static void updateSlotModifiers(Player player, EquipmentSlot slot, ItemStack stack) {
        UUID temperatureUuid = TemperatureUtilInternal.equipmentSlotTemperatureUuid.get(slot);
        UUID bodyResistanceUuid = BodyDamageUtilInternal.equipmentSlotBodyResistanceUuid.get(slot);
        boolean supportedStack = !stack.isEmpty() && ItemUtil.canBeEquippedInSlot(stack, slot);
        ResourceLocation itemId = supportedStack ? BuiltInRegistries.ITEM.getKey(stack.getItem()) : null;

        JsonTemperatureResistance temperature = null;
        if (Config.Baked.temperatureEnabled && itemId != null) {
            temperature = new JsonTemperatureResistance();
            for (AttributeModifierBase modifier : TemperatureModifierRegistry.ITEM_ATTRIBUTE_MODIFIERS_REGISTRY) {
                temperature.add(modifier.getItemAttributes(stack));
            }
        }
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
