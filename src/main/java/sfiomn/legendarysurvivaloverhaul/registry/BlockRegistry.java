package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.registries.BuiltInRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.*;
import sfiomn.legendarysurvivaloverhaul.common.blocks.airquality.SafetyLanternBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.airquality.SignalTorchBlock;
import sfiomn.legendarysurvivaloverhaul.common.blocks.airquality.WallSignalTorchBlock;

import java.util.function.Supplier;

public class BlockRegistry {

	public static final FabricDeferredRegister<Block> BLOCKS = FabricDeferredRegister.create(BuiltInRegistries.BLOCK);

	public static final RegistryObject<Block> HEATER = registerBlock("heater", () -> new HeaterBaseBlock(ThermalTypeEnum.HEATING));
	public static final RegistryObject<Block> HEATER_TOP = BLOCKS.register("heater_top", HeaterTopBlock::new);
	public static final RegistryObject<Block> COOLER = registerBlock("cooler", () -> new CoolerBlock(ThermalTypeEnum.COOLING));
	public static final RegistryObject<Block> SEWING_TABLE = registerBlock("sewing_table", SewingTableBlock::new);
	public static final RegistryObject<Block> SUN_FERN_CROP = BLOCKS.register("sun_fern_crop", SunFernBlock::new);
	public static final RegistryObject<Block> SUN_FERN_GOLD = registerBlock("sun_fern_gold", SunFernGoldBlock::new);
	public static final RegistryObject<Block> ICE_FERN_CROP = BLOCKS.register("ice_fern_crop", IceFernBlock::new);
	public static final RegistryObject<Block> ICE_FERN_GOLD = registerBlock("ice_fern_gold", IceFernGoldBlock::new);
	public static final RegistryObject<Block> WATER_PLANT_CROP = BLOCKS.register("water_plant_crop", WaterPlantBlock::new);

	// Air Quality
	public static final RegistryObject<Block> SAFETY_LANTERN = registerBlock("safety_lantern", () -> new SafetyLanternBlock(
			BlockBehaviour.Properties.copy(Blocks.LANTERN)
					.lightLevel(state -> state.getValue(SafetyLanternBlock.AIR_QUALITY).getLightLevel())));
	public static final RegistryObject<Block> SIGNAL_TORCH = BLOCKS.register("signal_torch", () -> new SignalTorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH)));
	public static final RegistryObject<Block> WALL_SIGNAL_TORCH = BLOCKS.register("wall_signal_torch", () -> new WallSignalTorchBlock(BlockBehaviour.Properties.copy(Blocks.WALL_TORCH)));

	private static <T extends Block> RegistryObject<Block> registerBlock(String name, Supplier<T> block) {
		RegistryObject<Block> newBlock = BLOCKS.register(name, block);
		registerBlockItem(name, newBlock);
		return newBlock;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
		ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register(){
		BLOCKS.registerAll();
	}
}
