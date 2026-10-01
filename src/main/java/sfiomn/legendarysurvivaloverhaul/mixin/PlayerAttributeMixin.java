package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

@Mixin(Player.class)
public abstract class PlayerAttributeMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"))
    private static void legendarysurvivaloverhaul$addAttributes(
            CallbackInfoReturnable<AttributeSupplier.Builder> callback) {
        AttributeSupplier.Builder attributes = callback.getReturnValue();
        attributes
                .add(AttributeRegistry.HEATING_TEMPERATURE.get())
                .add(AttributeRegistry.COOLING_TEMPERATURE.get())
                .add(AttributeRegistry.HEAT_RESISTANCE.get())
                .add(AttributeRegistry.COLD_RESISTANCE.get())
                .add(AttributeRegistry.THERMAL_RESISTANCE.get())
                .add(AttributeRegistry.BODY_RESISTANCE.get())
                .add(AttributeRegistry.HEAD_RESISTANCE.get())
                .add(AttributeRegistry.CHEST_RESISTANCE.get())
                .add(AttributeRegistry.RIGHT_ARM_RESISTANCE.get())
                .add(AttributeRegistry.LEFT_ARM_RESISTANCE.get())
                .add(AttributeRegistry.LEGS_RESISTANCE.get())
                .add(AttributeRegistry.FEET_RESISTANCE.get())
                .add(AttributeRegistry.BROKEN_HEART.get())
                .add(AttributeRegistry.PERMANENT_HEART.get())
                .add(AttributeRegistry.BROKEN_HEART_RESILIENCE.get());
    }
}
