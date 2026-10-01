package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricDamageHooks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {
    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float legendarysurvivaloverhaul$modifyIncomingDamage(float amount, DamageSource source) {
        return FabricDamageHooks.modifyIncomingDamage((LivingEntity) (Object) this, source, amount);
    }
}
