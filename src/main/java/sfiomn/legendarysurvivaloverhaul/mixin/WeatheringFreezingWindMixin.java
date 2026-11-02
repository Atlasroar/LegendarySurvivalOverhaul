package sfiomn.legendarysurvivaloverhaul.mixin;

import com.ordana.immersive_weathering.wind.FreezingWindEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import sfiomn.legendarysurvivaloverhaul.common.integration.weathering.WeatheringStorms;

@Mixin(FreezingWindEntity.class)
public abstract class WeatheringFreezingWindMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;push(DDD)V"))
    private void legendarysurvivaloverhaul$freezingWindHit(LivingEntity target, double x, double y, double z) {
        target.push(x, y, z);
        if (target instanceof Player player)
            WeatheringStorms.onFreezingWindHit(player);
    }
}
