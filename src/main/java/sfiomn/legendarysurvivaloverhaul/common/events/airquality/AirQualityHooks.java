package sfiomn.legendarysurvivaloverhaul.common.events.airquality;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class AirQualityHooks {
    private AirQualityHooks() {
    }

    public static void onAirSupplyTick(LivingEntity entity, int originalAirSupply) {
        if (!Config.Baked.airQualityEnabled || !AirQualityUtil.isSensitiveToAirQuality(entity)) {
            return;
        }

        if (entity.level().isClientSide) {
            entity.setAirSupply(originalAirSupply);
            return;
        }

        int airChange = AirQualityUtil.getAirQualityAtLocation(entity).getAirAmountAfterProtection(entity);
        int newAirSupply = Math.min(entity.getMaxAirSupply(), originalAirSupply + airChange);

        // Vanilla's own drowning damage is only ever applied inside LivingEntity#baseTick's
        // "eye in water" branch, so air-quality-driven depletion (bad air pockets, lava fumes,
        // Nether ambience, etc.) needs its own suffocation damage when the entity isn't actually
        // submerged in water; otherwise air can run out with no consequence at all.
        if (newAirSupply <= -20 && !isEyeInWater(entity)) {
            newAirSupply = 0;
            entity.hurt(entity.damageSources().drown(), 2.0F);
        }

        entity.setAirSupply(newAirSupply);
    }

    private static boolean isEyeInWater(LivingEntity entity) {
        return entity.isEyeInFluid(FluidTags.WATER);
    }
}
