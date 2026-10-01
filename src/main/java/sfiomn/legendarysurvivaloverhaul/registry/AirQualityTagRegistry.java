package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

/**
 * Tags backing the air quality system, adapted from Fuzss' MIT-licensed "Thin Air" mod
 * (https://github.com/Fuzss/thinair).
 */
public class AirQualityTagRegistry {

    public static final TagKey<Item> AIR_REFILLER_ITEM_TAG = TagKey.create(Registries.ITEM, id("air_refiller"));
    public static final TagKey<EntityType<?>> AIR_QUALITY_SENSITIVE_ENTITY_TYPE_TAG = TagKey.create(Registries.ENTITY_TYPE, id("air_quality_sensitive"));

    private static ResourceLocation id(String path) {
        return new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, path);
    }
}
