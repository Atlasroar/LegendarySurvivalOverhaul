package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricDamageHooks;

@Mixin(Player.class)
public abstract class PlayerDamageMixin {
    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$applyLocalizedBodyDamage(
            DamageSource source, float amount, CallbackInfo callback) {
        if ((Object) this instanceof Player player)
            FabricDamageHooks.onPlayerActuallyHurt(player, source, amount);
    }
}
