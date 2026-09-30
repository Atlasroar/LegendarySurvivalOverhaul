package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.CoolerBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.HeaterBlockEntity;

public class BlockEntityRegistry {
    public static FabricDeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            FabricDeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE);

    public static RegistryObject<BlockEntityType<HeaterBlockEntity>> HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("heater_block_entity", () -> BlockEntityType.Builder
                    .of(HeaterBlockEntity::new, BlockRegistry.HEATER.get()).build(null));

    public static RegistryObject<BlockEntityType<CoolerBlockEntity>> COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cooler_block_entity", () -> BlockEntityType.Builder
                    .of(CoolerBlockEntity::new, BlockRegistry.COOLER.get()).build(null));

    public static void register() {
        BLOCK_ENTITIES.registerAll();
    }
}
