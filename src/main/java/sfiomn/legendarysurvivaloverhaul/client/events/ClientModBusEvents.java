package sfiomn.legendarysurvivaloverhaul.client.events;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.renderer.RenderType;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.client.particles.BreathParticle;
import sfiomn.legendarysurvivaloverhaul.client.particles.FernBlossomParticle;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationClientTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.TooltipHandler;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

public final class ClientModBusEvents {
    private ClientModBusEvents() {
    }

    public static void register() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                BlockRegistry.ICE_FERN_CROP.get(),
                BlockRegistry.ICE_FERN_GOLD.get(),
                BlockRegistry.SUN_FERN_CROP.get(),
                BlockRegistry.SUN_FERN_GOLD.get(),
                BlockRegistry.WATER_PLANT_CROP.get());

        TooltipHandler.register();
        TooltipComponentCallback.EVENT.register(data ->
                data instanceof HydrationTooltipComponent hydration
                        ? new HydrationClientTooltipComponent(hydration.hydration, hydration.saturation)
                        : null);

        ParticleFactoryRegistry.getInstance().register(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ParticleTypeRegistry.COLD_BREATH.get(), BreathParticle.Factory::new);
    }
}
