package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.io.IOException;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker("loadEffect")
    void legendarysurvivaloverhaul$loadEffect(ResourceLocation effect) throws IOException;
}
