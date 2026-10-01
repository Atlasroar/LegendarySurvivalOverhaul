package sfiomn.legendarysurvivaloverhaul.client.integration.trinkets;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import sfiomn.legendarysurvivaloverhaul.client.render.AirQualityRespiratorRenderer;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public final class AirQualityTrinketsClientIntegration {
    private AirQualityTrinketsClientIntegration() {
    }

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(
                AirQualityRespiratorRenderer.MODEL_LAYER, AirQualityRespiratorRenderer::createLayer);
        TrinketRendererRegistry.registerRenderer(ItemRegistry.RESPIRATOR.get(),
                (stack, slotReference, entityModel, poseStack, buffer, packedLight,
                 entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch) ->
                        AirQualityRespiratorRenderer.render(stack, entityModel, poseStack, buffer, packedLight));
    }
}
