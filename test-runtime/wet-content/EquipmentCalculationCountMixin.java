package lso.test;

import lso.check.EquipmentCheck;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemAttributeModifier;

@Mixin({ItemAttributeModifier.class, CoatAttributeModifier.class})
public abstract class EquipmentCalculationCountMixin {
    @Inject(method = "getItemAttributes", at = @At("HEAD"), remap = false)
    private void test$countCalculation(ItemStack stack, CallbackInfoReturnable<JsonTemperatureResistance> cir) {
        EquipmentCheck.recordCalculation();
    }
}
