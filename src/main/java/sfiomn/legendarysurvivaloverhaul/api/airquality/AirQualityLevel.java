package sfiomn.legendarysurvivaloverhaul.api.airquality;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AirQualityTagRegistry;
import com.mojang.serialization.Codec;

import java.util.Locale;
import java.util.Objects;

/**
 * Represents the quality of the breathable air at a location, adapted from Fuzss' MIT-licensed "Thin Air" mod
 * (https://github.com/Fuzss/thinair) for Legendary Survival Overhaul's Fabric port.
 * <p>
 * GREEN: full freedom to breathe, air supply regenerates. BLUE: no air is lost nor regenerated.
 * YELLOW: air is slowly lost unless wearing a Respirator. RED: air is lost exactly like underwater.
 */
public enum AirQualityLevel implements StringRepresentable {
    GREEN(true, true, null) {
        @Override
        boolean isProtectedByEffect() {
            return false;
        }

        @Override
        int getAirAmount(LivingEntity entity) {
            return 4;
        }
    },
    BLUE(true, false, null) {
        @Override
        boolean isProtectedByEffect() {
            return false;
        }

        @Override
        int getAirAmount(LivingEntity entity) {
            return 0;
        }
    },
    YELLOW(false, false, "breathing_equipment") {
        @Override
        int getAirAmount(LivingEntity entity) {
            long drainInterval = entity.level().dimension() == Level.NETHER ? 2L : 4L;
            return entity.level().getGameTime() % drainInterval == 0 ? super.getAirAmount(entity) : 0;
        }
    },
    RED(false, false, "heavy_breathing_equipment");

    public static final Codec<AirQualityLevel> CODEC = StringRepresentable.fromEnum(AirQualityLevel::values);

    public final boolean canBreathe;
    public final boolean canRefillAir;
    @Nullable
    private final TagKey<Item> breathingEquipment;
    private final TagKey<Block> airProviders;

    AirQualityLevel(boolean canBreathe, boolean canRefillAir, @Nullable String breathingEquipment) {
        this.canBreathe = canBreathe;
        this.canRefillAir = canRefillAir;
        this.breathingEquipment = breathingEquipment != null ?
                TagKey.create(Registries.ITEM, id(breathingEquipment)) :
                null;
        this.airProviders = TagKey.create(Registries.BLOCK, id(this.getSerializedName() + "_air_providers"));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, path);
    }

    @Nullable
    public static AirQualityLevel getAirQualityAtEyes(BlockState blockState) {
        if (blockState.is(Blocks.BUBBLE_COLUMN)) return GREEN;
        if (!blockState.getFluidState().isEmpty()) return RED;
        return getAirQualityFromBlock(blockState);
    }

    @Nullable
    public static AirQualityLevel getAirQualityFromBlock(BlockState blockState) {
        if (blockState.hasProperty(BlockStateProperties.LIT) && !blockState.getValue(BlockStateProperties.LIT)) {
            return null;
        }
        for (AirQualityLevel airQualityLevel : AirQualityLevel.values()) {
            if (blockState.is(airQualityLevel.airProviders)) {
                return airQualityLevel;
            }
        }
        return null;
    }

    public TagKey<Item> getBreathingEquipment() {
        Objects.requireNonNull(this.breathingEquipment, "breathing equipment is null");
        return this.breathingEquipment;
    }

    public TagKey<Block> getAirProvidersTag() {
        return this.airProviders;
    }

    public double getAirProviderRadius() {
        return switch (this) {
            case RED -> Config.Baked.redAirProviderRadius;
            case GREEN -> Config.Baked.greenAirProviderRadius;
            case YELLOW -> Config.Baked.yellowAirProviderRadius;
            case BLUE -> Config.Baked.blueAirProviderRadius;
        };
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public int getLightLevel() {
        return 15 - this.ordinal() * 3;
    }

    public int getOutputSignal() {
        return this.ordinal() + 1;
    }

    public float getItemModelProperty() {
        return this.ordinal() / 10.0F;
    }

    public boolean isBetterThan(AirQualityLevel other) {
        return this.ordinal() < other.ordinal();
    }

    boolean isProtectedByEffect() {
        return true;
    }

    boolean isProtected(LivingEntity entity) {
        if (!this.isProtectedByEffect()) return false;
        return MobEffectUtil.hasWaterBreathing(entity) ||
                (entity.isUsingItem() && entity.getUseItem().is(AirQualityTagRegistry.AIR_REFILLER_ITEM_TAG)) ||
                this.isProtectedViaBreathingEquipment(entity);
    }

    private boolean isProtectedViaBreathingEquipment(LivingEntity entity) {
        if (this.breathingEquipment == null) return false;
        ItemStack itemStack = AirQualityUtil.findEquippedBreathingEquipment(entity, this.breathingEquipment);
        if (!itemStack.isEmpty() && entity.level().getGameTime() % (20L * 15L) == 0) {
            itemStack.hurtAndBreak(1, entity, livingEntity -> livingEntity.broadcastBreakEvent(EquipmentSlot.HEAD));
        }
        return !itemStack.isEmpty();
    }

    int getAirAmount(LivingEntity entity) {
        return entity.getRandom().nextInt(EnchantmentHelper.getRespiration(entity) + 1) == 0 ? -1 : 0;
    }

    public int getAirAmountAfterProtection(LivingEntity entity) {
        return this.isProtected(entity) ? 0 : this.getAirAmount(entity);
    }
}
