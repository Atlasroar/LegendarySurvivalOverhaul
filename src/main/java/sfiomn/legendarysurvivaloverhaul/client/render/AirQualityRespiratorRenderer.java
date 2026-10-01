package sfiomn.legendarysurvivaloverhaul.client.render;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.function.Function;

public final class AirQualityRespiratorRenderer {
    public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "player"), "respirator");
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            LegendarySurvivalOverhaul.MOD_ID, "textures/models/armor/respirator_layer_1.png");
    private static AirQualityRespiratorRenderer instance;

    private final HumanoidModel<LivingEntity> model;

    private AirQualityRespiratorRenderer(EntityModelSet modelSet) {
        this.model = new HumanoidModel<>(modelSet.bakeLayer(MODEL_LAYER)) {
            @Override
            protected Iterable<ModelPart> headParts() {
                return ImmutableList.of(this.head);
            }

            @Override
            protected Iterable<ModelPart> bodyParts() {
                return ImmutableList.of();
            }
        };
    }

    public static LayerDefinition createLayer() {
        return LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.02F), 0.0F), 64, 32);
    }

    @SuppressWarnings("unchecked")
    public static void render(ItemStack stack, EntityModel<? extends LivingEntity> entityModel,
                              PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!(entityModel instanceof HumanoidModel<?> humanoidModel)) {
            return;
        }
        if (instance == null) {
            instance = new AirQualityRespiratorRenderer(Minecraft.getInstance().getEntityModels());
        }
        ((HumanoidModel<LivingEntity>) humanoidModel).copyPropertiesTo(instance.model);
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(
                buffer, RenderType.armorCutoutNoCull(TEXTURE), false, stack.hasFoil());
        instance.model.renderToBuffer(poseStack, vertexConsumer, packedLight,
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
