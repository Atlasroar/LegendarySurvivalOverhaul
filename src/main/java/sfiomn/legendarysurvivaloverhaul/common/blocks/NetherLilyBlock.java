package sfiomn.legendarysurvivaloverhaul.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

// Adapted for LSO on 2026-11-02 from Scorchful's NetherLilyBlock, CrimsonLilyBlock and NetherLilyBehaviours (LGPL-3.0).
public class NetherLilyBlock extends Block {
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 3);
    public static final TagKey<Block> ABSORBS_WATER = TagKey.create(Registries.BLOCK,
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "nether_lily_can_absorb_water"));
    public static final TagKey<EntityType<?>> HURTS = TagKey.create(Registries.ENTITY_TYPE,
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "crimson_lily_hurts"));
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 3, 14);
    private final boolean crimson;

    public NetherLilyBlock(boolean crimson) {
        super(Properties.copy(Blocks.CRIMSON_ROOTS).randomTicks());
        this.crimson = crimson;
        registerDefaultState(stateDefinition.any().setValue(WATER_LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATER_LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP) && level.getFluidState(pos).isEmpty();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !canSurvive(state, level, pos)
                ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(WATER_LEVEL) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(WATER_LEVEL) < 3 && random.nextInt(7) == 0
                && (!level.dimensionType().ultraWarm() || level.getBlockState(pos.below()).is(ABSORBS_WATER)))
            setWater(level, pos, state, state.getValue(WATER_LEVEL) + 1);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        CanteenItem canteen = stack.getItem() instanceof CanteenItem item ? item : null;
        if (!crimson && state.getValue(WATER_LEVEL) == 3
                && (stack.is(Items.GLASS_BOTTLE) || canteen != null && canteen.canFill(stack))) {
            if (!level.isClientSide) {
                if (canteen != null)
                    canteen.fill(stack);
                else
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player,
                            PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                setWater(level, pos, state, 0);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1, 1);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        boolean waterBottle = stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER;
        if (crimson && state.getValue(WATER_LEVEL) < 3
                && (waterBottle || canteen != null && CanteenItem.canDrink(stack))) {
            if (!level.isClientSide) {
                if (canteen != null) {
                    if (!player.getAbilities().instabuild)
                        CanteenItem.shrinkCapacity(stack);
                } else {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                }
                setWater(level, pos, state, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1, 1);
                level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!crimson || level.isClientSide || state.getValue(WATER_LEVEL) != 3)
            return;
        if (entity instanceof Player player && WetnessCapability.canGetWet(player))
            CapabilityUtil.getWetnessCapability(player).setWetness(WetnessCapability.WETNESS_LIMIT);
        if (entity.getType().is(HURTS))
            entity.hurt(level.damageSources().generic(), 10);
        setWater(level, pos, state, 0);
        level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1, 1);
        ((ServerLevel) level).sendParticles(ParticleTypes.SPLASH,
                pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 40, 0.3, 0.3, 0.3, 0.1);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(WATER_LEVEL) == 3)
            level.addParticle(ParticleTypes.DRIPPING_WATER, pos.getX() + random.nextDouble(),
                    pos.getY() + 0.25, pos.getZ() + random.nextDouble(), 0, 0, 0);
    }

    private static void setWater(Level level, BlockPos pos, BlockState state, int water) {
        BlockState updated = state.setValue(WATER_LEVEL, water);
        level.setBlockAndUpdate(pos, updated);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(updated));
    }
}
