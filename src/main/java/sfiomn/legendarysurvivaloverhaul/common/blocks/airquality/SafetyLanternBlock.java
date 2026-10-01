package sfiomn.legendarysurvivaloverhaul.common.blocks.airquality;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;

/**
 * A lantern that visually reflects the current air quality at its location, changing texture/light level/dye to
 * match. Can be locked to a dye color with a matching dye item so it stops auto-updating, and unlocked again with
 * an axe. Adapted from Fuzss' MIT-licensed "Thin Air" mod (https://github.com/Fuzss/thinair).
 */
public class SafetyLanternBlock extends LanternBlock {
    public static final EnumProperty<AirQualityLevel> AIR_QUALITY = EnumProperty.create("air_quality", AirQualityLevel.class);
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final String TAG_AIR_QUALITY_LEVEL = "AirQualityLevel";

    public SafetyLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AIR_QUALITY, AirQualityLevel.GREEN).setValue(LOCKED, false));
    }

    private static BlockState setAirQuality(Level level, BlockPos pos, BlockState template) {
        return template.setValue(AIR_QUALITY, AirQualityUtil.computeAirQualityAtLocation(level, Vec3.atCenterOf(pos)));
    }

    public static ItemStack getDisplayItemStack(AirQualityLevel airQualityLevel) {
        ItemStack itemStack = new ItemStack(sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry.SAFETY_LANTERN.get());
        itemStack.getOrCreateTag().putInt(TAG_AIR_QUALITY_LEVEL, airQualityLevel.ordinal());
        return itemStack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AIR_QUALITY, LOCKED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        blockState = setAirQuality(context.getLevel(), context.getClickedPos(), blockState);

        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState result = blockState.setValue(HANGING, direction == Direction.UP);
                if (result.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return result;
                }
            }
        }

        return null;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity placer, @NotNull ItemStack itemStack) {
        level.scheduleTick(blockPos, this, 20, TickPriority.NORMAL);
    }

    @Override
    @NotNull
    public InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        ItemStack itemUsed = player.getItemInHand(hand);

        AirQualityLevel presentLockedAirQuality = blockState.getValue(LOCKED) ? blockState.getValue(AIR_QUALITY) : null;
        AirQualityLevel lockedAirQuality = null;
        boolean strippedDye = false;

        if (itemUsed.is(Items.GREEN_DYE) && presentLockedAirQuality != AirQualityLevel.GREEN) {
            lockedAirQuality = AirQualityLevel.GREEN;
        } else if (itemUsed.is(Items.BLUE_DYE) && presentLockedAirQuality != AirQualityLevel.BLUE) {
            lockedAirQuality = AirQualityLevel.BLUE;
        } else if (itemUsed.is(Items.YELLOW_DYE) && presentLockedAirQuality != AirQualityLevel.YELLOW) {
            lockedAirQuality = AirQualityLevel.YELLOW;
        } else if (itemUsed.is(Items.RED_DYE) && presentLockedAirQuality != AirQualityLevel.RED) {
            lockedAirQuality = AirQualityLevel.RED;
        } else if (itemUsed.getItem() instanceof AxeItem && blockState.getValue(LOCKED)) {
            strippedDye = true;
        }

        boolean didAnything = false;
        BlockState newState = blockState;
        if (lockedAirQuality != null) {
            newState = newState.setValue(AIR_QUALITY, lockedAirQuality).setValue(LOCKED, true);
            level.levelEvent(player, 3003, blockPos, 0);
            if (!player.getAbilities().instabuild) {
                itemUsed.shrink(1);
            }
            didAnything = true;
        } else if (strippedDye) {
            level.playSound(player, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockPos, 0);
            itemUsed.hurtAndBreak(1, player, livingEntity -> livingEntity.broadcastBreakEvent(hand));
            player.swing(hand);
            newState = newState.setValue(LOCKED, false);
            newState = setAirQuality(level, blockPos, newState);
            didAnything = true;
        }

        level.setBlockAndUpdate(blockPos, newState);

        return didAnything ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }

    @Override
    public void tick(@NotNull BlockState blockState, @NotNull ServerLevel level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        level.scheduleTick(blockPos, this, 20, TickPriority.NORMAL);
        if (!blockState.getValue(LOCKED)) {
            level.setBlockAndUpdate(blockPos, setAirQuality(level, blockPos, blockState));
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {
        return blockState.getValue(AIR_QUALITY).getOutputSignal();
    }
}
