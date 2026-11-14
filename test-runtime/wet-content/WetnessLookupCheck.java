package lso.check;

import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.TickPhase;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class WetnessLookupCheck {
    private static final BlockPos POS = new BlockPos(8, 200, 8);
    private static final ResourceLocation WOODEN_ID = new ResourceLocation("meadow", "wooden_water_cauldron");
    private static Block woodenCauldron;
    private static boolean active;
    private static int particleChecks;
    private static final Map<BlockPos, Integer> FLUID_READS = new HashMap<>();
    private static final Map<BlockPos, Integer> BLOCK_READS = new HashMap<>();

    public static void register() {
        woodenCauldron = BuiltInRegistries.BLOCK.containsKey(WOODEN_ID)
                ? BuiltInRegistries.BLOCK.get(WOODEN_ID)
                : Registry.register(BuiltInRegistries.BLOCK, WOODEN_ID, new LayeredCauldronBlock(
                        BlockBehaviour.Properties.copy(Blocks.WATER_CAULDRON),
                        LayeredCauldronBlock.RAIN, CauldronInteraction.WATER));
    }

    public static void recordFluid(BlockPos pos) {
        if (active)
            FLUID_READS.merge(pos.immutable(), 1, Integer::sum);
    }

    public static void recordBlock(BlockPos pos) {
        if (active)
            BLOCK_READS.merge(pos.immutable(), 1, Integer::sum);
    }

    public static void recordParticleCheck() {
        if (active)
            particleChecks++;
    }

    private static void reset() {
        FLUID_READS.clear();
        BLOCK_READS.clear();
        particleChecks = 0;
    }

    private static void require(boolean condition, String message) {
        if (!condition)
            throw new AssertionError(message);
    }

    public static void run(ServerLevel level) {
        ServerPlayer player = new ServerPlayer(level.getServer(), level,
                new GameProfile(UUID.randomUUID(), "WetnessLookup"));
        player.connection = new ServerGamePacketListenerImpl(level.getServer(),
                new Connection(PacketFlow.SERVERBOUND), player);
        player.setPos(8.5, 200, 8.5);
        WetnessCapability wet = CapabilityUtil.getWetnessCapability(player);
        int timer = Config.Baked.wetnessTickTimer;
        int decrease = Config.Baked.wetnessDecrease;
        int rain = Config.Baked.wetnessRainIncrease;
        int fluid = Config.Baked.wetnessFluidIncrease;
        boolean enabled = Config.Baked.wetnessEnabled;
        boolean meadow = LegendarySurvivalOverhaul.meadowLoaded;
        var mounts = Config.Baked.wetnessImmunityMounts;
        try {
            Config.Baked.wetnessEnabled = true;
            Config.Baked.wetnessTickTimer = 5;
            Config.Baked.wetnessDecrease = -3;
            Config.Baked.wetnessRainIncrease = 5;
            Config.Baked.wetnessFluidIncrease = 20;
            Config.Baked.wetnessImmunityMounts = List.of();
            level.setWeatherParameters(0, 0, false, false);
            level.setRainLevel(0);
            for (int y = POS.getY(); y < level.getMaxBuildHeight(); y++)
                level.setBlockAndUpdate(new BlockPos(POS.getX(), y, POS.getZ()), Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(POS.below(), Blocks.STONE.defaultBlockState());

            environment(level, player, Blocks.WATER.defaultBlockState(), Blocks.AIR.defaultBlockState());
            checkUpdate(level, player, wet, 100, 120, 4, "Water with air above");
            checkUpdate(level, player, wet, 400, 400, 4, "Full wetness remains capped");
            level.setBlockAndUpdate(POS.above(), Blocks.WATER.defaultBlockState());
            checkUpdate(level, player, wet, 100, 140, 4, "Two-block water column");
            environment(level, player, Blocks.LAVA.defaultBlockState(), Blocks.AIR.defaultBlockState());
            checkUpdate(level, player, wet, 100, 80, 4, "Same-position change to lava is immediate");
            level.setBlockAndUpdate(POS.above(), Blocks.LAVA.defaultBlockState());
            checkUpdate(level, player, wet, 100, 60, 4, "Two-block lava drying");
            environment(level, player, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState());
            checkUpdate(level, player, wet, 100, 97, 3, "Same-position change to dry air");
            checkUpdate(level, player, wet, 0, 0, 2, "Dry player does not invoke particles");
            player.setRemainingFireTicks(20);
            checkUpdate(level, player, wet, 100, 87, 3, "Fire drying precedes environment drying");
            player.clearFire();

            for (Block cauldron : new Block[] {Blocks.WATER_CAULDRON, woodenCauldron}) {
                LegendarySurvivalOverhaul.meadowLoaded = cauldron == woodenCauldron;
                for (int fill = 1; fill <= 3; fill++) {
                    environment(level, player, cauldron.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, fill),
                            Blocks.AIR.defaultBlockState());
                    player.setPos(8.5, 200, 8.5);
                    checkUpdate(level, player, wet, 100, 100 + (fill - 1) * 10, 3, "Cauldron fill " + fill);
                }
                level.setBlockAndUpdate(POS, cauldron.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 2));
                player.setPos(8.5, 200.5625, 8.5);
                checkUpdate(level, player, wet, 100, 110, 3, "Cauldron surface tolerance boundary");
                player.setPos(8.5, 200.57, 8.5);
                checkUpdate(level, player, wet, 100, 100, 3, "Above cauldron surface stays unchanged");
            }
            LegendarySurvivalOverhaul.meadowLoaded = meadow;
            player.setPos(8.5, 200.4, 8.5);
            environment(level, player, Blocks.WATER.defaultBlockState().setValue(net.minecraft.world.level.block.LiquidBlock.LEVEL, 4),
                    Blocks.AIR.defaultBlockState());
            checkUpdate(level, player, wet, 100, 109, 4, "Fractional flowing-water depth");
            player.setPos(8.5, 200.6, 8.5);
            checkUpdate(level, player, wet, 100, 100, 4, "Above flowing-water surface");
            player.setPos(8.5, 200, 8.5);
            environment(level, player, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState());
            level.setWeatherParameters(0, 200, true, false);
            level.setRainLevel(1);
            // Avoid pending asynchronous skylight updates from the preceding block replacements.
            player.setPos(8.5, level.getMaxBuildHeight() + 1, 8.5);
            for (var biomeKey : List.of(Biomes.PLAINS, Biomes.SNOWY_PLAINS)) {
                var biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(biomeKey);
                level.getChunk(0, 0).fillBiomesFromNoise((x, y, z, sampler) -> biome,
                        level.getChunkSource().randomState().sampler());
                checkUpdate(level, player, wet, 100, biomeKey.equals(Biomes.PLAINS) ? 105 : 100, 3,
                        "Rain versus snow precipitation");
            }
            level.setWeatherParameters(0, 0, false, false);
            level.setRainLevel(0);
            player.setPos(8.5, 200, 8.5);

            var boat = EntityType.BOAT.create(level);
            require(boat != null && player.startRiding(boat, true), "Boat fixture mounts");
            boat.hasImpulse = false;
            player.setPos(8.5, 199, 8.5);
            environment(level, player, Blocks.WATER.defaultBlockState(), Blocks.AIR.defaultBlockState());
            checkUpdate(level, player, wet, 100, 97, 3, "Floating boat ignores water at feet");
            level.setBlockAndUpdate(POS, Blocks.WATER.defaultBlockState());
            checkUpdate(level, player, wet, 100, 120, 3, "Floating boat particle check uses shifted block only");
            boat.hasImpulse = true;
            checkUpdate(level, player, wet, 100, 140, 4, "Impulsed boat keeps unshifted calculation");
            Config.Baked.wetnessImmunityMounts = List.of("minecraft:boat");
            wet.setWetnessTickTimer(4);
            noLookups(player, level, wet, TickPhase.END, 100, 97, "Protected mount still dries");
            Config.Baked.wetnessImmunityMounts = List.of();
            player.stopRiding();
            boat.discard();
            var pig = EntityType.PIG.create(level);
            require(pig != null && player.startRiding(pig, true), "Living mount fixture");
            pig.hasImpulse = false;
            player.setPos(8.5, 199, 8.5);
            checkUpdate(level, player, wet, 100, 140, 4, "Living mount does not shift fluid position");
            player.stopRiding();
            pig.discard();

            for (TickPhase phase : TickPhase.values()) {
                wet.setWetnessTickTimer(-1);
                noLookups(player, level, wet, phase, 100, 0, "Deactivation is immediate on " + phase);
                require(wet.getWetnessTickTimer() == -1, "Deactivation remains set");
                wet.setWetnessTickTimer(0);
                for (InteractionHand hand : InteractionHand.values()) {
                    player.setItemInHand(hand, new ItemStack(ItemRegistry.SPONGE.get()));
                    noLookups(player, level, wet, phase, 100, 0, "Held Sponge is immediate on " + phase);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }
                for (GameType mode : new GameType[] {GameType.CREATIVE, GameType.SPECTATOR}) {
                    player.setGameMode(mode);
                    noLookups(player, level, wet, phase, 100, 0, "Mode immunity on " + phase);
                }
                player.setGameMode(GameType.SURVIVAL);
            }
            wet.setWetnessTickTimer(0);
            int packetTimer = wet.getPacketTimer();
            noLookups(player, level, wet, TickPhase.START, 100, 100, "START only updates packet timer");
            require(wet.getPacketTimer() == packetTimer + 1 && wet.getWetnessTickTimer() == 0, "START timing preserved");
            noLookups(player, level, wet, TickPhase.END, 100, 100, "Not-yet-due END skips environment");
            require(wet.getWetnessTickTimer() == 1, "END timer still increments");
            if (FabricLoader.getInstance().isModLoaded("trinkets")) {
                require(TrinketsUtil.equipTrinket(player, new ItemStack(ItemRegistry.SPONGE.get())), "Real Sponge equips");
                for (TickPhase phase : TickPhase.values())
                    noLookups(player, level, wet, phase, 100, 0, "Equipped Sponge immunity on " + phase);
            }
            System.out.println("WETNESS_LOOKUP_CHECK_PASSED fluid_reads=4->2 block_reads=1->1");
        } finally {
            active = false;
            reset();
            player.stopRiding();
            level.setWeatherParameters(0, 0, false, false);
            level.setRainLevel(0);
            Config.Baked.wetnessTickTimer = timer;
            Config.Baked.wetnessDecrease = decrease;
            Config.Baked.wetnessRainIncrease = rain;
            Config.Baked.wetnessFluidIncrease = fluid;
            Config.Baked.wetnessEnabled = enabled;
            Config.Baked.wetnessImmunityMounts = mounts;
            LegendarySurvivalOverhaul.meadowLoaded = meadow;
        }
    }

    private static void environment(ServerLevel level, ServerPlayer player, BlockState lower, BlockState upper) {
        BlockPos pos = player.blockPosition();
        level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        level.setBlockAndUpdate(pos, lower);
        level.setBlockAndUpdate(pos.above(), upper);
        require(level.getBlockState(pos).equals(lower) && level.getBlockState(pos.above()).equals(upper),
                "Environment setup must not mix water and lava");
    }

    private static void checkUpdate(ServerLevel level, ServerPlayer player, WetnessCapability wet,
                                    int start, int expected, int oldReads, String message) {
        reset();
        active = true;
        boolean referenceParticles;
        BlockPos pos = player.blockPosition();
        try {
            if (player.getVehicle() != null && !(player.getVehicle() instanceof LivingEntity)
                    && !player.getVehicle().hasImpulse) {
                pos = pos.above();
                referenceParticles = start > 0 && level.getFluidState(pos).isEmpty();
            } else {
                referenceParticles = start > 0
                        && (level.getFluidState(pos).isEmpty() || level.getFluidState(pos.above()).isEmpty());
            }
            level.getFluidState(pos);
            level.getBlockState(pos);
            level.getFluidState(pos.above());
        } finally {
            active = false;
        }
        require(FLUID_READS.values().stream().mapToInt(Integer::intValue).sum() == oldReads, message + ": reference reads");
        reset();
        wet.setWetness(start);
        wet.setWetnessTickTimer(4);
        active = true;
        try {
            wet.tickUpdate(player, level, TickPhase.END);
        } finally {
            active = false;
        }
        require(wet.getWetness() == expected, message + ": " + wet.getWetness() + " != " + expected);
        require(FLUID_READS.size() == 2 && FLUID_READS.getOrDefault(pos, 0) == 1
                        && FLUID_READS.getOrDefault(pos.above(), 0) == 1,
                message + ": fluid state is read once per position");
        require(BLOCK_READS.size() == 1 && BLOCK_READS.getOrDefault(pos, 0) == 1,
                message + ": block read remains unchanged");
        require(particleChecks == (referenceParticles ? 1 : 0), message + ": original particle decision");
    }

    private static void noLookups(ServerPlayer player, ServerLevel level, WetnessCapability wet,
                                  TickPhase phase, int start, int expected, String message) {
        reset();
        wet.setWetness(start);
        active = true;
        try {
            wet.tickUpdate(player, level, phase);
        } finally {
            active = false;
        }
        require(wet.getWetness() == expected && FLUID_READS.isEmpty() && BLOCK_READS.isEmpty() && particleChecks == 0,
                message);
    }
}
