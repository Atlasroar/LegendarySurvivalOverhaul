package sfiomn.legendarysurvivaloverhaul.api.airquality;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.AirQualityDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AirQualityTagRegistry;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Computes the {@link AirQualityLevel} a living entity is currently breathing. Adapted from Fuzss' MIT-licensed
 * "Thin Air" mod (https://github.com/Fuzss/thinair). Unlike upstream, this implementation does not keep a
 * persistent per-chunk capability of air-provider block positions (the original scanning/capability system was
 * acknowledged upstream as unreliable); instead it performs a bounded nearby-block scan for air provider blocks,
 * cached briefly per entity to keep the cost of the lookup low.
 */
public final class AirQualityUtil {
    private static final int CACHE_DURATION_TICKS = 10;
    private static final Map<LivingEntity, CachedResult> CACHE = new WeakHashMap<>();

    private AirQualityUtil() {
    }

    public static AirQualityLevel getAirQualityAtLocation(LivingEntity entity) {
        if (!Config.Baked.airQualityEnabled) return AirQualityLevel.GREEN;
        Vec3 location = entity.getEyePosition();
        BlockPos eyePosition = BlockPos.containing(location);
        ResourceLocation dimension = entity.level().dimension().location();
        long gameTime = entity.level().getGameTime();
        synchronized (CACHE) {
            CachedResult cached = CACHE.get(entity);
            if (cached != null
                    && cached.dimension.equals(dimension)
                    && cached.blockPosition.equals(eyePosition)
                    && gameTime >= cached.computedAtTick
                    && gameTime - cached.computedAtTick < CACHE_DURATION_TICKS) {
                return cached.level;
            }
        }
        AirQualityLevel level = computeAirQualityAtLocation(entity.level(), location);
        synchronized (CACHE) {
            CACHE.put(entity, new CachedResult(level, dimension, eyePosition, gameTime));
        }
        return level;
    }

    public static AirQualityLevel computeAirQualityAtLocation(Level level, Vec3 location) {
        if (!Config.Baked.airQualityEnabled) return AirQualityLevel.GREEN;
        BlockPos eyePosition = BlockPos.containing(location);
        BlockState blockAtEyes = level.getBlockState(eyePosition);
        AirQualityLevel airQualityAtEyes = AirQualityLevel.getAirQualityAtEyes(blockAtEyes);
        if (airQualityAtEyes != null) return airQualityAtEyes;

        AirQualityLevel bestNearbyQuality = scanForNearbyAirProvider(level, location);
        if (bestNearbyQuality != null) return bestNearbyQuality;

        return AirQualityDataManager.getAirQualityAtLevelByDimension(level, eyePosition.getY());
    }

    /**
     * Scans a bounded radius for any block tagged as an air provider (lanterns, soul fire, portals, lava...),
     * returning the best (lowest ordinal) quality found within its own configured radius. Lookup results are cached
     * briefly per entity; administrators should keep configured radii reasonable because the search volume grows
     * cubically with radius.
     */
    private static AirQualityLevel scanForNearbyAirProvider(Level level, Vec3 location) {
        AirQualityLevel best = null;
        double maxRadius = 0;
        for (AirQualityLevel airQualityLevel : AirQualityLevel.values()) {
            maxRadius = Math.max(maxRadius, airQualityLevel.getAirProviderRadius());
        }
        int radius = (int) Math.ceil(maxRadius);
        BlockPos center = BlockPos.containing(location);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    mutable.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    if (!level.isLoaded(mutable)) continue;
                    BlockState blockState = level.getBlockState(mutable);
                    AirQualityLevel candidate = AirQualityLevel.getAirQualityFromBlock(blockState);
                    if (candidate == null) continue;
                    double distanceSq = Vec3.atCenterOf(mutable).distanceToSqr(location);
                    if (distanceSq > candidate.getAirProviderRadius() * candidate.getAirProviderRadius()) continue;
                    if (candidate == AirQualityLevel.GREEN) return AirQualityLevel.GREEN;
                    if (best == null || candidate.isBetterThan(best)) best = candidate;
                }
            }
        }
        return best;
    }

    public static boolean isSensitiveToAirQuality(LivingEntity entity) {
        return entity.getType().is(AirQualityTagRegistry.AIR_QUALITY_SENSITIVE_ENTITY_TYPE_TAG) && (
                !(entity instanceof Player player) || !player.getAbilities().invulnerable);
    }

    public static ItemStack findEquippedBreathingEquipment(LivingEntity entity, TagKey<Item> tagKey) {
        ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.is(tagKey)) return headItem;

        if (entity instanceof Player player && LegendarySurvivalOverhaul.trinketsLoaded) {
            ItemStack trinketItem = TrinketsUtil.findEquippedItem(player, tagKey);
            if (!trinketItem.isEmpty()) return trinketItem;
        }

        return ItemStack.EMPTY;
    }

    private record CachedResult(AirQualityLevel level, ResourceLocation dimension, BlockPos blockPosition,
                                long computedAtTick) {
    }
}
