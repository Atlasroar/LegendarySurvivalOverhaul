package lso.test;

import lso.check.AirScanCheck;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerChunkCache.class)
public abstract class AirChunkLookupMixin {
    @Inject(method = "hasChunk", at = @At("HEAD"))
    private void test$countLoadedCheck(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        AirScanCheck.recordChunkLookup(x, z, false, true);
    }

    @Inject(method = "getChunk(IILnet/minecraft/world/level/chunk/ChunkStatus;Z)Lnet/minecraft/world/level/chunk/ChunkAccess;",
            at = @At("HEAD"))
    private void test$countChunkLookup(int x, int z, ChunkStatus status, boolean create,
                                      CallbackInfoReturnable<ChunkAccess> cir) {
        AirScanCheck.recordChunkLookup(x, z, create, false);
    }
}
