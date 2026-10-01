package sfiomn.legendarysurvivaloverhaul.common.blocks.airquality;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Wall-mounted counterpart of {@link SignalTorchBlock}. Adapted from Fuzss' MIT-licensed "Thin Air" mod
 * (https://github.com/Fuzss/thinair).
 */
public class WallSignalTorchBlock extends WallTorchBlock {

    public WallSignalTorchBlock(Properties properties) {
        super(properties, ParticleTypes.FLAME);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        Direction facing = blockState.getValue(FACING);
        Direction opposite = facing.getOpposite();
        double x = blockPos.getX() + 0.5D + 0.27D * opposite.getStepX();
        double y = blockPos.getY() + 0.92D;
        double z = blockPos.getZ() + 0.5D + 0.27D * opposite.getStepZ();
        double dx = (random.nextDouble() - 0.5) * 0.05 + facing.getStepX() * 0.01;
        double dy = random.nextDouble() * 0.1;
        double dz = (random.nextDouble() - 0.5) * 0.05 + facing.getStepZ() * 0.01;
        level.addParticle(ParticleTypes.FIREWORK, x, y, z, dx, dy, dz);
        level.addParticle(this.flameParticle, x, y, z, 0.0D, 0.0D, 0.0D);
    }
}
