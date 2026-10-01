package sfiomn.legendarysurvivaloverhaul.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.mixin.GameRendererAccessor;
import sfiomn.legendarysurvivaloverhaul.mixin.PostChainAccessor;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.io.IOException;
import java.util.List;

public class RenderBlurOverlay {

    private static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blobs2.json");
    private static final float DEFAULT_SHADER_INTENSITY = 0;
    private static final float MAX_SHADER_INTENSITY = 4;
    private static final float SHADER_INTENSITY_STEP = 0.05f;
    private static final int HYDRATION_LEVEL_MIN_EFFECT = 6;
    private static final int HYDRATION_LEVEL_MAX_EFFECT = 2;
    private static float shaderIntensity = 0;
    private static int updateTimer = 0;
    private static boolean hasShownBlurWarning = false;

    public static void render(Player player) {
        if (player.isSpectator() || player.isCreative() || shaderIntensity == 0
                || Minecraft.getInstance().screen instanceof DeathScreen) {
            stop();
            return;
        }

        var gameRenderer = Minecraft.getInstance().gameRenderer;
        PostChain currentEffect = gameRenderer.currentEffect();
        if (currentEffect == null) {
            try {
                ((GameRendererAccessor) gameRenderer).legendarysurvivaloverhaul$loadEffect(BLUR_SHADER);
                currentEffect = gameRenderer.currentEffect();
            } catch (IOException exception) {
                LegendarySurvivalOverhaul.LOGGER.error("Unable to load the low-hydration blur shader", exception);
                return;
            }
        }

        if (currentEffect == null || !BLUR_SHADER.toString().equals(currentEffect.getName()))
            return;

        List<PostPass> passes = ((PostChainAccessor) currentEffect).legendarysurvivaloverhaul$getPasses();
        if (passes.isEmpty())
            return;
        EffectInstance effect = passes.get(0).getEffect();
        var radius = effect.getUniform("Radius");
        if (radius != null)
            radius.set(shaderIntensity);
    }

    public static void stop() {
        PostChain currentEffect = Minecraft.getInstance().gameRenderer.currentEffect();
        if (currentEffect != null && BLUR_SHADER.toString().equals(currentEffect.getName()))
            Minecraft.getInstance().gameRenderer.shutdownEffect();
    }

    public static void updateBlurIntensity(Player player) {
        float targetShaderIntensity = DEFAULT_SHADER_INTENSITY;
        if (player != null && player.isAlive() && !player.isCreative() && !player.isSpectator()) {

            ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
            // hydration is 0 - 20
            int hydration = thirstCap.getHydrationLevel();

            if (hydration <= HYDRATION_LEVEL_MIN_EFFECT) {
                targetShaderIntensity = (1 - ((float) (hydration - HYDRATION_LEVEL_MAX_EFFECT) / (float) (HYDRATION_LEVEL_MIN_EFFECT - HYDRATION_LEVEL_MAX_EFFECT))) * MAX_SHADER_INTENSITY;
                
                // Show first-time blur warning
                if (!hasShownBlurWarning && targetShaderIntensity > 0) {
                    player.displayClientMessage(Component.literal("Vision is getting blurry. Drink some water"), true);
                    hasShownBlurWarning = true;
                }
            }

            if (updateTimer++ % 2 == 0) {
                if (targetShaderIntensity > shaderIntensity) {
                    shaderIntensity = Math.min(shaderIntensity + SHADER_INTENSITY_STEP, targetShaderIntensity);
                } else if (targetShaderIntensity < shaderIntensity) {
                    shaderIntensity = Math.max(shaderIntensity - SHADER_INTENSITY_STEP, targetShaderIntensity);
                }
            }
        } else {
            shaderIntensity = 0;
        }
    }
}
