package sfiomn.legendarysurvivaloverhaul.common.integration.vanillabackport;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public final class VanillaBackportCompat {
    private static final boolean LOADED = FabricLoader.getInstance().isModLoaded("vanillabackport");
    private static final ResourceKey<Biome> SULFUR_CAVES = ResourceKey.create(
            Registries.BIOME, new ResourceLocation("minecraft", "sulfur_caves"));

    private VanillaBackportCompat() {}

    public static boolean hasSulfurAir(Level level, BlockPos eyePosition) {
        return LOADED && Config.Baked.airQualityEnabled && Config.Baked.sulfurCaveAirEnabled
                && level.getBiome(eyePosition).is(SULFUR_CAVES);
    }

    public static boolean blocksSulfurNausea(LivingEntity entity) {
        if (!LOADED || !Config.Baked.respiratorBlocksSulfurNausea) return false;
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is(ItemRegistry.RESPIRATOR.get())) return true;
        return entity instanceof Player player && TrinketsApi.getTrinketComponent(player)
                .map(component -> component.isEquipped(ItemRegistry.RESPIRATOR.get()))
                .orElse(false);
    }
}
