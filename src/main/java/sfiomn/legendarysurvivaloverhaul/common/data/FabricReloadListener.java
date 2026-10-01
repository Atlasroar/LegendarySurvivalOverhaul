package sfiomn.legendarysurvivaloverhaul.common.data;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

public final class FabricReloadListener implements IdentifiableResourceReloadListener {
    private final ResourceLocation id;
    private final PreparableReloadListener delegate;

    private FabricReloadListener(ResourceLocation id, PreparableReloadListener delegate) {
        this.id = id;
        this.delegate = delegate;
    }

    public static void register(ResourceLocation id, PreparableReloadListener listener) {
        ResourceManagerHelper.get(PackType.SERVER_DATA)
                .registerReloadListener(new FabricReloadListener(id, listener));
    }

    @Override
    public ResourceLocation getFabricId() {
        return id;
    }

    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier barrier,
            ResourceManager resourceManager,
            ProfilerFiller preparationsProfiler,
            ProfilerFiller reloadProfiler,
            Executor backgroundExecutor,
            Executor gameExecutor) {
        return delegate.reload(barrier, resourceManager, preparationsProfiler, reloadProfiler,
                backgroundExecutor, gameExecutor);
    }
}
