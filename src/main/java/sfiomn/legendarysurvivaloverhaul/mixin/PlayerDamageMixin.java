package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricDamageHooks;

@Mixin(Player.class)
public abstract class PlayerDamageMixin {
    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float legendarysurvivaloverhaul$applyHealthOverhaul(
            float amount, DamageSource source) {
        if ((Object) this instanceof Player player) {
            if (!player.level().isClientSide && !player.isCreative() && !player.isSpectator()
                    && Config.Baked.healthOverhaulEnabled) {
                amount = HealthUtil.hurtPlayer(player, amount);
            }
            FabricDamageHooks.onPlayerActuallyHurt(player, source, amount);
        }
        return amount;
    }
}
