package sfiomn.legendarysurvivaloverhaul.api.airquality;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.AirQualityDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.vanillabackport.VanillaBackportCompat;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AirQualityTagRegistry;

import java.util.IdentityHashMap;
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

    public static void invalidateCache() {
        synchronized (CACHE) {
            CACHE.clear();
        }
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

        if (VanillaBackportCompat.hasSulfurAir(level, eyePosition)) return AirQualityLevel.YELLOW;

        return AirQualityDataManager.getAirQualityAtLevelByDimension(level, eyePosition.getY());
    }

    /**
     * Scans a bounded radius for any block tagged as an air provider (lanterns, soul fire, portals, lava...),
     * returning the worst (highest ordinal, e.g. RED over GREEN) quality found within its own configured radius.
     * Nearby hazards (lava, bad air pockets) must always win over a nearby safe source (a portal, a lantern):
     * standing between a lava pool and a portal should still poison you, not get "healed" by the portal simply
     * being in range too. Lookup results are cached briefly per entity; administrators should keep configured radii
     * reasonable because the search volume grows cubically with radius.
     */
    private static AirQualityLevel scanForNearbyAirProvider(Level level, Vec3 location) {
        AirQualityLevel worst = null;
        AirQualityLevel[] qualities = AirQualityLevel.values();
        double[] radiiSquared = new double[qualities.length];
        double maxRadius = 0;
        for (AirQualityLevel quality : qualities) {
            double providerRadius = quality.getAirProviderRadius();
            radiiSquared[quality.ordinal()] = providerRadius * providerRadius;
            maxRadius = Math.max(maxRadius, providerRadius);
        }
        double maxRadiusSquared = maxRadius * maxRadius;
        int radius = (int) Math.ceil(maxRadius);
        BlockPos center = BlockPos.containing(location);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int minChunkX = (center.getX() - radius) >> 4;
        int minChunkZ = (center.getZ() - radius) >> 4;
        int chunkCountX = ((center.getX() + radius) >> 4) - minChunkX + 1;
        int chunkCountZ = ((center.getZ() + radius) >> 4) - minChunkZ + 1;
        ChunkAccess[] chunks = new ChunkAccess[chunkCountX * chunkCountZ];
        boolean[] checkedChunks = new boolean[chunks.length];
        // Block states are shared palette entries; memoize only for this scan so tag reloads remain visible.
        Map<BlockState, Integer> providerQualities = new IdentityHashMap<>();
        for (int dx = -radius; dx <= radius; dx++) {
            int blockX = center.getX() + dx;
            int chunkX = blockX >> 4;
            int chunkRow = (chunkX - minChunkX) * chunkCountZ;
            double xDistance = blockX + 0.5 - location.x;
            double xDistanceSquared = xDistance * xDistance;
            if (xDistanceSquared > maxRadiusSquared) continue;
            for (int dy = -radius; dy <= radius; dy++) {
                int blockY = center.getY() + dy;
                if (level.isOutsideBuildHeight(blockY)) continue;
                double yDistance = blockY + 0.5 - location.y;
                double xyDistanceSquared = xDistanceSquared + yDistance * yDistance;
                if (xyDistanceSquared > maxRadiusSquared) continue;
                for (int dz = -radius; dz <= radius; dz++) {
                    int blockZ = center.getZ() + dz;
                    double zDistance = blockZ + 0.5 - location.z;
                    double distanceSq = xyDistanceSquared + zDistance * zDistance;
                    if (distanceSq > maxRadiusSquared) continue;
                    int chunkZ = blockZ >> 4;
                    int chunkIndex = chunkRow + chunkZ - minChunkZ;
                    if (!checkedChunks[chunkIndex]) {
                        checkedChunks[chunkIndex] = true;
                        if (level.getChunkSource().hasChunk(chunkX, chunkZ))
                            chunks[chunkIndex] = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                    }
                    ChunkAccess chunk = chunks[chunkIndex];
                    if (chunk == null) continue;
                    mutable.set(blockX, blockY, blockZ);
                    BlockState blockState = chunk.getBlockState(mutable);
                    Integer candidateIndex = providerQualities.get(blockState);
                    if (candidateIndex == null) {
                        AirQualityLevel candidate = AirQualityLevel.getAirQualityFromBlock(blockState);
                        if (blockState.getFluidState().is(FluidTags.LAVA)) {
                            candidate = AirQualityLevel.RED;
                        }
                        candidateIndex = candidate == null ? -1 : candidate.ordinal();
                        providerQualities.put(blockState, candidateIndex);
                    }
                    if (candidateIndex < 0 || distanceSq > radiiSquared[candidateIndex]) continue;
                    AirQualityLevel candidate = qualities[candidateIndex];
                    if (candidate == AirQualityLevel.RED) return AirQualityLevel.RED;
                    if (worst == null || candidate.ordinal() > worst.ordinal()) worst = candidate;
                }
            }
        }
        return worst;
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
