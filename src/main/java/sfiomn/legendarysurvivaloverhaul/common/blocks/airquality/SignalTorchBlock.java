package sfiomn.legendarysurvivaloverhaul.common.blocks.airquality;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * A cosmetic torch variant toggled from a normal torch via right-click, giving off extra flame particles.
 * Adapted from Fuzss' MIT-licensed "Thin Air" mod (https://github.com/Fuzss/thinair).
 */
public class SignalTorchBlock extends TorchBlock {

    public SignalTorchBlock(Properties properties) {
        super(properties, ParticleTypes.FLAME);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        double x = blockPos.getX() + 0.5D;
        double y = blockPos.getY() + 0.7D;
        double z = blockPos.getZ() + 0.5D;
        double dx = (random.nextDouble() - 0.5) * 0.05;
        double dy = random.nextDouble() * 0.1;
        double dz = (random.nextDouble() - 0.5) * 0.05;
        level.addParticle(ParticleTypes.FIREWORK, x, y, z, dx, dy, dz);
        level.addParticle(this.flameParticle, x, y, z, 0.0D, 0.0D, 0.0D);
    }
}
