package sfiomn.legendarysurvivaloverhaul.common.events.airquality;

import net.minecraft.world.entity.LivingEntity;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class AirQualityHooks {
    private AirQualityHooks() {
    }

    public static void onAirSupplyTick(LivingEntity entity, int originalAirSupply) {
        if (!Config.Baked.airQualityEnabled || entity.level().isClientSide || !AirQualityUtil.isSensitiveToAirQuality(entity)) {
            return;
        }

        int airChange = AirQualityUtil.getAirQualityAtLocation(entity).getAirAmountAfterProtection(entity);
        entity.setAirSupply(Math.min(entity.getMaxAirSupply(), originalAirSupply + airChange));
    }
}
