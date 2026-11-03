package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

// Blade geometry adapted for LSO on 2026-11-02 from Frostiful's IceSkateModel (LGPL-3.0).
public final class IceSkateRenderer implements ArmorRenderer {
    private static final ModelLayerLocation BLADES = new ModelLayerLocation(
            id("ice_skates"), "blades");
    private static final ModelLayerLocation BOOTS = new ModelLayerLocation(
            id("ice_skates"), "boots");
    private EntityModelSet modelSet;
    private HumanoidModel<LivingEntity> boots;
    private HumanoidModel<LivingEntity> bladeModel;

    private IceSkateRenderer() {}

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(BLADES, IceSkateRenderer::blades);
        EntityModelLayerRegistry.registerModelLayer(BOOTS,
                () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1), 0), 64, 32));
        ArmorRenderer.register(new IceSkateRenderer(), ItemRegistry.ICE_SKATES.get());
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource buffers, ItemStack stack, LivingEntity entity,
                       EquipmentSlot slot, int light, HumanoidModel<LivingEntity> context) {
        var models = Minecraft.getInstance().getEntityModels();
        if (models != modelSet) {
            modelSet = models;
            boots = new HumanoidModel<>(models.bakeLayer(BOOTS));
            bladeModel = new HumanoidModel<>(models.bakeLayer(BLADES));
        }
        context.copyPropertiesTo(boots);
        context.copyPropertiesTo(bladeModel);
        boots.setAllVisible(false);
        bladeModel.setAllVisible(false);
        boots.leftLeg.visible = boots.rightLeg.visible = true;
        bladeModel.leftLeg.visible = bladeModel.rightLeg.visible = true;
        ArmorRenderer.renderPart(matrices, buffers, light, stack, boots, id("textures/models/armor/snow_layer_1.png"));
        ArmorRenderer.renderPart(matrices, buffers, light, stack, bladeModel, id("textures/entity/skates.png"));
    }

    private static LayerDefinition blades() {
        var mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        mesh.getRoot().addOrReplaceChild("right_leg", blade(false), PartPose.offset(-1.9F, 12, 0));
        mesh.getRoot().addOrReplaceChild("left_leg", blade(true), PartPose.offset(1.9F, 12, 0));
        return LayerDefinition.create(mesh, 32, 32);
    }

    private static CubeListBuilder blade(boolean mirror) {
        return CubeListBuilder.create().mirror(mirror)
                .texOffs(0, 0).addBox(-1, 13, -4, 2, 0, 8)
                .texOffs(8, 0).addBox(0, 13, -4, 0, 1, 8)
                .texOffs(0, 2).addBox(-1, 11, -4, 2, 2, 0)
                .texOffs(0, 0).addBox(-1, 11, 4, 2, 2, 0);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, path);
    }
}
