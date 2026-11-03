package sfiomn.legendarysurvivaloverhaul.common.items.armor;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.UUID;

// Movement values and walking penalty adapted for LSO on 2026-11-02 from Frostiful's IceSkater (LGPL-3.0).
public final class IceSkating {
    public static final TagKey<Item> SKATES = TagKey.create(Registries.ITEM,
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "ice_skates"));
    public static final UUID WALK_PENALTY_ID = UUID.fromString("749e2aa7-e69d-4c5f-a024-779a4ac0c485");
    private static final AttributeModifier WALK_PENALTY = new AttributeModifier(WALK_PENALTY_ID,
            "LSO ice skate walking penalty", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

    private IceSkating() {}

    public static boolean wearingSkates(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).is(SKATES);
    }

    public static boolean skating(LivingEntity entity, BlockState floor) {
        return wearingSkates(entity) && floor.is(BlockTags.ICE) && !entity.isSpectator()
                && !entity.isPassenger() && (entity.getPose() == Pose.STANDING || entity.getPose() == Pose.CROUCHING);
    }

    public static float friction(LivingEntity entity, BlockState floor, float original) {
        if (!skating(entity, floor))
            return original;
        return entity.isShiftKeyDown() ? 1.0F : entity.isSprinting() ? 1.078F : 1.075F;
    }

    public static void updateWalkingPenalty(LivingEntity entity, BlockState floor) {
        var speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null)
            return;
        boolean slowed = wearingSkates(entity) && entity.onGround() && !floor.is(BlockTags.ICE) && !entity.isSpectator();
        if (slowed && !speed.hasModifier(WALK_PENALTY))
            speed.addTransientModifier(WALK_PENALTY);
        else if (!slowed && speed.hasModifier(WALK_PENALTY))
            speed.removeModifier(WALK_PENALTY_ID);
    }
}
