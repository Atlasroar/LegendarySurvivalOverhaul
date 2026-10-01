package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public final class FabricInteractionCallbacks {
    private FabricInteractionCallbacks() {
    }

    public static void register() {
        UseBlockCallback.EVENT.register(FabricInteractionCallbacks::onUseBlock);
    }

    private static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        InteractionResult signalTorchResult = tryToggleSignalTorch(player, level, hand, hit);
        if (signalTorchResult != InteractionResult.PASS) return signalTorchResult;

        if (shouldApplyThirst(player) && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            ThirstCapability thirst = CapabilityUtil.getThirstCapability(player);
            if (!thirst.isHydrationLevelAtMax()) {
                if (level.getBlockEntity(hit.getBlockPos()) instanceof MenuProvider)
                    return InteractionResult.PASS;

                JsonThirstBlock thirstBlock = ThirstUtil.getBlockThirstLookedAt(player, 3.0);
                JsonThirstBlock thirstFluid = ThirstUtil.getFluidThirstLookedAt(player, 3.0);

                if (hasHydration(thirstBlock) && !player.isCrouching()) {
                    drinkAtBlock(player, thirstBlock, level);
                    return InteractionResult.CONSUME;
                }

                if (hasHydration(thirstFluid))
                    drinkAtBlock(player, thirstFluid, level);
            }
        }

        if (Config.Baked.temperatureEnabled && hand == InteractionHand.MAIN_HAND && !level.isClientSide) {
            BlockState usedBlock = level.getBlockState(hit.getBlockPos());
            TemperatureUtil.applyConsumableBlockTemperature(player, usedBlock);
        }

        return InteractionResult.PASS;
    }

    private static boolean shouldApplyThirst(Player player) {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }

    /**
     * Toggles a plain torch/wall torch into its cosmetic Signal Torch variant and back, when right-clicked
     * empty-handed. Adapted from Fuzss' MIT-licensed "Thin Air" mod (https://github.com/Fuzss/thinair).
     */
    private static InteractionResult tryToggleSignalTorch(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        if (!Config.Baked.airQualityEnabled || !Config.Baked.enableSignalTorches || hand != InteractionHand.MAIN_HAND
                || !player.getItemInHand(hand).isEmpty() || player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        BlockPos blockPos = hit.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);

        Block nextBlock = null;
        float pitch = 1.0f;
        if (blockState.is(Blocks.TORCH)) {
            nextBlock = BlockRegistry.SIGNAL_TORCH.get();
        } else if (blockState.is(Blocks.WALL_TORCH)) {
            nextBlock = BlockRegistry.WALL_SIGNAL_TORCH.get();
        } else if (blockState.is(BlockRegistry.SIGNAL_TORCH.get())) {
            nextBlock = Blocks.TORCH;
            pitch = 0.8f;
        } else if (blockState.is(BlockRegistry.WALL_SIGNAL_TORCH.get())) {
            nextBlock = Blocks.WALL_TORCH;
            pitch = 0.8f;
        }

        if (nextBlock == null) return InteractionResult.PASS;

        BlockState nextState = nextBlock.defaultBlockState();
        if (blockState.hasProperty(WallTorchBlock.FACING)) {
            nextState = nextState.setValue(WallTorchBlock.FACING, blockState.getValue(WallTorchBlock.FACING));
        }
        level.setBlockAndUpdate(blockPos, nextState);
        level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, pitch);
        player.swing(hand);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static boolean hasHydration(JsonThirstBlock thirst) {
        return thirst != null && (thirst.hydration != 0 || thirst.saturation != 0);
    }

    private static void drinkAtBlock(Player player, JsonThirstBlock thirst, Level level) {
        if (level.isClientSide)
            playerDrinkEffect(player);
        else
            ThirstUtil.takeDrink(player, thirst.hydration, thirst.saturation, thirst.effects);
    }

    private static void playerDrinkEffect(Player player) {
        player.swing(InteractionHand.MAIN_HAND);
        player.playSound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }
}
