package lso.check;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.AirQualityDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.vanillabackport.VanillaBackportCompat;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import java.util.Random;
import java.util.UUID;

public final class AirScanCheck {
    private static boolean counting;
    private static int classifications;

    public static void recordClassification() {
        if (counting) classifications++;
    }

    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    public static void run(ServerLevel level) {
        double green = Config.Baked.greenAirProviderRadius;
        double blue = Config.Baked.blueAirProviderRadius;
        double yellow = Config.Baked.yellowAirProviderRadius;
        double red = Config.Baked.redAirProviderRadius;
        boolean enabled = Config.Baked.airQualityEnabled;
        long time = level.getGameTime();
        Vec3 eyes = new Vec3(-8.5, 128.5, -8.5);
        BlockPos center = BlockPos.containing(eyes);
        try {
            Config.Baked.airQualityEnabled = true;
            Config.Baked.greenAirProviderRadius = 9;
            Config.Baked.blueAirProviderRadius = 6;
            Config.Baked.yellowAirProviderRadius = 6;
            Config.Baked.redAirProviderRadius = 3;
            for (int x = -3; x <= 1; x++) {
                for (int z = -3; z <= 1; z++) level.getChunk(x, z);
            }
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.GREEN,
                    "Empty test volume starts in breathable air");
            counting = true;
            classifications = 0;
            reference(level, eyes);
            int previousClassifications = classifications;
            classifications = 0;
            AirQualityUtil.computeAirQualityAtLocation(level, eyes);
            int optimizedClassifications = classifications;
            counting = false;
            check(previousClassifications == 6860, "Reference visits the default 19-cubed scan plus eyes");
            check(optimizedClassifications <= 4,
                    "Empty-air scan classifies shared states once, not thousands of times");

            BlockPos portal = center.offset(2, 0, 0);
            BlockPos lava = center.offset(-3, 0, 0);
            level.setBlock(portal, Blocks.NETHER_PORTAL.defaultBlockState(), 2);
            level.setBlock(lava, Blocks.LAVA.defaultBlockState(), 2);
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.RED,
                    "Lava wins over a safe portal");
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes.add(1.0e-7, 0, 0)) == AirQualityLevel.GREEN,
                    "Lava just outside its own radius cannot use the portal's larger radius");
            level.setBlock(lava, Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(portal, Blocks.AIR.defaultBlockState(), 2);

            BlockPos campfire = center.offset(2, 1, 0);
            level.setBlock(campfire, Blocks.SOUL_CAMPFIRE.defaultBlockState()
                    .setValue(BlockStateProperties.LIT, false), 2);
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.GREEN,
                    "Unlit provider is ignored");
            level.setBlock(campfire, Blocks.SOUL_CAMPFIRE.defaultBlockState(), 2);
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.BLUE,
                    "Lit state is classified separately");
            level.setBlock(campfire, Blocks.AIR.defaultBlockState(), 2);

            level.setBlock(center, Blocks.BUBBLE_COLUMN.defaultBlockState(), 2);
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.GREEN,
                    "Bubble column at eyes still takes precedence");
            level.setBlock(center, Blocks.WATER.defaultBlockState(), 2);
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.RED,
                    "Water at eyes remains unbreathable");
            level.setBlock(center, Blocks.AIR.defaultBlockState(), 2);

            Random random = new Random(73942);
            for (int i = 0; i < 48; i++) {
                BlockPos source = center.offset(random.nextInt(19) - 9,
                        random.nextInt(19) - 9, random.nextInt(19) - 9);
                var state = switch (i % 4) {
                    case 0 -> Blocks.SOUL_TORCH.defaultBlockState();
                    case 1 -> Blocks.NETHER_PORTAL.defaultBlockState();
                    case 2 -> Blocks.LAVA.defaultBlockState().setValue(BlockStateProperties.LEVEL, 3);
                    default -> Blocks.SOUL_CAMPFIRE.defaultBlockState()
                            .setValue(BlockStateProperties.LIT, false);
                };
                level.setBlock(source, state, 2);
                Config.Baked.blueAirProviderRadius = 1 + random.nextDouble() * 8;
                Config.Baked.redAirProviderRadius = 1 + random.nextDouble() * 4;
                Vec3 position = eyes.add(random.nextDouble() - 0.5,
                        random.nextDouble() - 0.5, random.nextDouble() - 0.5);
                check(AirQualityUtil.computeAirQualityAtLocation(level, position) == reference(level, position),
                        "Fractional radius/eye-position parity, negative coordinates and flowing lava: " + i);
                level.setBlock(source, Blocks.AIR.defaultBlockState(), 2);
            }
            Config.Baked.blueAirProviderRadius = 6;
            Config.Baked.redAirProviderRadius = 3;
            Config.Baked.blueAirProviderRadius = 32;
            BlockPos farProvider = center.offset(32, 0, 0);
            level.setBlock(farProvider, Blocks.SOUL_TORCH.defaultBlockState(), 2);
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes) == AirQualityLevel.BLUE
                            && reference(level, eyes) == AirQualityLevel.BLUE,
                    "Maximum configured radius includes its exact boundary");
            check(AirQualityUtil.computeAirQualityAtLocation(level, eyes.add(-1.0e-7, 0, 0))
                            == AirQualityLevel.GREEN,
                    "Maximum-radius provider excludes positions just beyond the boundary");
            level.setBlock(farProvider, Blocks.AIR.defaultBlockState(), 2);
            Config.Baked.blueAirProviderRadius = 6;
            Vec3 unloaded = new Vec3(1000000.5, 128.5, -1000000.5);
            check(AirQualityUtil.computeAirQualityAtLocation(level, unloaded) == reference(level, unloaded),
                    "Unloaded neighborhoods preserve ambient fallback");
            check(!level.hasChunk(62500, -62500), "Scan does not load absent chunks");

            ServerPlayer player = new ServerPlayer(level.getServer(), level,
                    new GameProfile(UUID.randomUUID(), "AirScanCheck"));
            player.setPos(eyes.x, eyes.y - player.getEyeHeight(), eyes.z);
            AirQualityUtil.invalidateCache();
            check(AirQualityUtil.getAirQualityAtLocation(player) == AirQualityLevel.GREEN,
                    "Initial entity cache");
            level.setBlock(lava, Blocks.LAVA.defaultBlockState(), 2);
            level.getServer().getWorldData().overworldData().setGameTime(time + 9);
            check(AirQualityUtil.getAirQualityAtLocation(player) == AirQualityLevel.GREEN,
                    "Existing sub-ten-tick cache timing is unchanged");
            level.getServer().getWorldData().overworldData().setGameTime(time + 10);
            check(AirQualityUtil.getAirQualityAtLocation(player) == AirQualityLevel.RED,
                    "Expired cache detects the new hazard");
            level.setBlock(lava, Blocks.AIR.defaultBlockState(), 2);
            AirQualityUtil.invalidateCache();
            check(AirQualityUtil.getAirQualityAtLocation(player) == AirQualityLevel.GREEN,
                    "Explicit config/data invalidation still works");
            level.setBlock(center.east(), Blocks.WATER.defaultBlockState(), 2);
            player.setPos(eyes.x + 1, player.getY(), eyes.z);
            check(AirQualityUtil.getAirQualityAtLocation(player) == AirQualityLevel.RED,
                    "Eye-block movement resolves a fresh location");
            level.setBlock(center.east(), Blocks.AIR.defaultBlockState(), 2);

            for (int i = 0; i < 40; i++) {
                reference(level, eyes);
                AirQualityUtil.computeAirQualityAtLocation(level, eyes);
            }
            long before = System.nanoTime();
            for (int i = 0; i < 200; i++) reference(level, eyes);
            long previousNanos = System.nanoTime() - before;
            before = System.nanoTime();
            for (int i = 0; i < 200; i++) AirQualityUtil.computeAirQualityAtLocation(level, eyes);
            long optimizedNanos = System.nanoTime() - before;
            System.out.printf("AIR_SCAN_CHECK_PASSED classifications=%d->%d reference_ms=%.2f optimized_ms=%.2f%n",
                    previousClassifications, optimizedClassifications,
                    previousNanos / 1.0e6, optimizedNanos / 1.0e6);
        } finally {
            counting = false;
            Config.Baked.airQualityEnabled = enabled;
            Config.Baked.greenAirProviderRadius = green;
            Config.Baked.blueAirProviderRadius = blue;
            Config.Baked.yellowAirProviderRadius = yellow;
            Config.Baked.redAirProviderRadius = red;
            level.getServer().getWorldData().overworldData().setGameTime(time);
            AirQualityUtil.invalidateCache();
        }
    }

    private static AirQualityLevel reference(ServerLevel level, Vec3 location) {
        BlockPos center = BlockPos.containing(location);
        AirQualityLevel atEyes = AirQualityLevel.getAirQualityAtEyes(level.getBlockState(center));
        if (atEyes != null) return atEyes;
        double max = 0;
        for (AirQualityLevel quality : AirQualityLevel.values()) max = Math.max(max, quality.getAirProviderRadius());
        int radius = (int) Math.ceil(max);
        AirQualityLevel worst = null;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    if (!level.isLoaded(pos)) continue;
                    var state = level.getBlockState(pos);
                    AirQualityLevel candidate = AirQualityLevel.getAirQualityFromBlock(state);
                    if (state.getFluidState().is(FluidTags.LAVA)) candidate = AirQualityLevel.RED;
                    if (candidate == null) continue;
                    if (Vec3.atCenterOf(pos).distanceToSqr(location)
                            > candidate.getAirProviderRadius() * candidate.getAirProviderRadius()) continue;
                    if (candidate == AirQualityLevel.RED) return candidate;
                    if (worst == null || candidate.ordinal() > worst.ordinal()) worst = candidate;
                }
            }
        }
        if (worst != null) return worst;
        if (VanillaBackportCompat.hasSulfurAir(level, center)) return AirQualityLevel.YELLOW;
        return AirQualityDataManager.getAirQualityAtLevelByDimension(level, center.getY());
    }
}
