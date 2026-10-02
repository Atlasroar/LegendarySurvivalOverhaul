package sfiomn.legendarysurvivaloverhaul.common.events.airquality;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Map;
import java.util.WeakHashMap;

public final class AirQualityHooks {
    // Diagnostic-only: logs a player's computed air quality level/air supply periodically and whenever the level
    // changes, to help pin down cases where the level resolves unexpectedly (e.g. appearing "frozen" with no
    // drain/regen) without live debugging.
    private static final int PERIODIC_LOG_INTERVAL_TICKS = 100;
    private static final Map<LivingEntity, AirQualityLevel> LAST_LOGGED_LEVEL = new WeakHashMap<>();
    private static final Map<LivingEntity, Long> LAST_LOGGED_TICK = new WeakHashMap<>();

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

        AirQualityLevel airQualityLevel = AirQualityUtil.getAirQualityAtLocation(entity);
        int airChange = airQualityLevel.getAirAmountAfterProtection(entity);
        logIfNeeded(entity, airQualityLevel, airChange);
        int newAirSupply = Math.min(entity.getMaxAirSupply(), originalAirSupply + airChange);

        // Vanilla's own drowning damage is only ever applied inside LivingEntity#baseTick's
        // "eye in water" branch, so air-quality-driven depletion (bad air pockets, lava fumes,
        // Nether ambience, etc.) needs its own suffocation damage when the entity isn't actually
        // submerged in water; otherwise air can run out with no consequence at all.
        if (isEyeInWater(entity)) {
            // Vanilla checks exactly -20; larger configured drain steps must not skip that threshold.
            newAirSupply = Math.max(-20, newAirSupply);
        } else if (newAirSupply <= -20) {
            newAirSupply = 0;
            if (Config.Baked.suffocationDamage > 0)
                entity.hurt(entity.damageSources().drown(), (float) Config.Baked.suffocationDamage);
        }

        entity.setAirSupply(newAirSupply);
    }

    private static boolean isEyeInWater(LivingEntity entity) {
        return entity.isEyeInFluid(FluidTags.WATER);
    }

    private static void logIfNeeded(LivingEntity entity, AirQualityLevel level, int airChange) {
        if (!(entity instanceof Player) || !LegendarySurvivalOverhaul.LOGGER.isDebugEnabled()) return;
        long gameTime = entity.level().getGameTime();
        AirQualityLevel previousLevel = LAST_LOGGED_LEVEL.get(entity);
        Long previousTick = LAST_LOGGED_TICK.get(entity);
        boolean levelChanged = previousLevel != level;
        boolean periodicDue = previousTick == null || gameTime - previousTick >= PERIODIC_LOG_INTERVAL_TICKS;
        if (!levelChanged && !periodicDue) return;

        LAST_LOGGED_LEVEL.put(entity, level);
        LAST_LOGGED_TICK.put(entity, gameTime);
        LegendarySurvivalOverhaul.        LOGGER.debug(
                "[AirQuality] {} breathing {} at {} in {} (airChange={}, airSupply={}, changed={})",
                entity.getName().getString(), level, entity.blockPosition(), entity.level().dimension().location(),
                airChange, entity.getAirSupply(), levelChanged);
    }
}
