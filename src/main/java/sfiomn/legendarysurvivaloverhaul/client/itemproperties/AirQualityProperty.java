package sfiomn.legendarysurvivaloverhaul.client.itemproperties;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;
import sfiomn.legendarysurvivaloverhaul.common.blocks.airquality.SafetyLanternBlock;

public class AirQualityProperty implements ClampedItemPropertyFunction {
    @Override
    public float unclampedCall(@NotNull ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains(SafetyLanternBlock.TAG_AIR_QUALITY_LEVEL, Tag.TAG_INT)) {
            int ordinal = tag.getInt(SafetyLanternBlock.TAG_AIR_QUALITY_LEVEL);
            if (ordinal >= 0 && ordinal < AirQualityLevel.values().length) {
                return AirQualityLevel.values()[ordinal].getItemModelProperty();
            }
        }

        if (entity != null) {
            return AirQualityUtil.getAirQualityAtLocation(entity).getItemModelProperty();
        }
        return AirQualityLevel.YELLOW.getItemModelProperty();
    }
}
