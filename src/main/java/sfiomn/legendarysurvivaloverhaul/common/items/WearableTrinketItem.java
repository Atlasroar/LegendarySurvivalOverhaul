package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import com.google.common.collect.Multimap;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketSlotConfig;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

import java.util.UUID;

public class WearableTrinketItem extends TrinketItem {

    public WearableTrinketItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return super.canEquip(stack, slot, entity) && TrinketSlotConfig.canEquip(stack, slot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getModifiers(
            ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getModifiers(stack, slot, entity, uuid);
        JsonTemperatureResistance temperature = TemperatureDataManager.getItem(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (temperature != null) {
            addModifier(modifiers, AttributeRegistry.HEATING_TEMPERATURE.get(), "heating_temperature",
                    Math.max(temperature.temperature, 0), uuid);
            addModifier(modifiers, AttributeRegistry.COOLING_TEMPERATURE.get(), "cooling_temperature",
                    Math.min(temperature.temperature, 0), uuid);
            addModifier(modifiers, AttributeRegistry.HEAT_RESISTANCE.get(), "heat_resistance",
                    temperature.heatResistance, uuid);
            addModifier(modifiers, AttributeRegistry.COLD_RESISTANCE.get(), "cold_resistance",
                    temperature.coldResistance, uuid);
            addModifier(modifiers, AttributeRegistry.THERMAL_RESISTANCE.get(), "thermal_resistance",
                    temperature.thermalResistance, uuid);
        }

        JsonBodyPartResistance bodyResistance = Config.Baked.localizedBodyDamageEnabled
                ? BodyDamageDataManager.getBodyResistanceItem(BuiltInRegistries.ITEM.getKey(stack.getItem())) : null;
        if (bodyResistance != null) {
            addModifier(modifiers, AttributeRegistry.BODY_RESISTANCE.get(), "body_resistance",
                    bodyResistance.bodyResistance, uuid);
            addModifier(modifiers, AttributeRegistry.HEAD_RESISTANCE.get(), "head_resistance",
                    bodyResistance.headResistance, uuid);
            addModifier(modifiers, AttributeRegistry.CHEST_RESISTANCE.get(), "chest_resistance",
                    bodyResistance.chestResistance, uuid);
            addModifier(modifiers, AttributeRegistry.RIGHT_ARM_RESISTANCE.get(), "right_arm_resistance",
                    bodyResistance.rightArmResistance, uuid);
            addModifier(modifiers, AttributeRegistry.LEFT_ARM_RESISTANCE.get(), "left_arm_resistance",
                    bodyResistance.leftArmResistance, uuid);
            addModifier(modifiers, AttributeRegistry.LEGS_RESISTANCE.get(), "legs_resistance",
                    bodyResistance.legsResistance, uuid);
            addModifier(modifiers, AttributeRegistry.FEET_RESISTANCE.get(), "feet_resistance",
                    bodyResistance.feetResistance, uuid);
        }
        return modifiers;
    }

    private static void addModifier(Multimap<Attribute, AttributeModifier> modifiers, Attribute attribute,
                                    String name, double value, UUID uuid) {
        modifiers.put(attribute, new AttributeModifier(uuid,
                "attribute.legendarysurvivaloverhaul." + name, value, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);

        boolean isWorn = TrinketsUtil.equipTrinket(player, itemstack);
        if (isWorn)
            level.playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, player.getSoundSource(), 1.0f, 1.0f);

        return isWorn ? InteractionResultHolder.success(itemstack): InteractionResultHolder.fail(itemstack);
    }
}
