package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import sfiomn.legendarysurvivaloverhaul.common.containers.AbstractThermalContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.CoolerContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.HeaterContainer;
import sfiomn.legendarysurvivaloverhaul.common.containers.SewingTableContainer;

public class ContainerRegistry {
    public static final FabricDeferredRegister<MenuType<?>> CONTAINERS =
            FabricDeferredRegister.create(BuiltInRegistries.MENU);

    public static final RegistryObject<MenuType<AbstractThermalContainer>> COOLER_CONTAINER
            = CONTAINERS.register("cooler_container", () -> new ExtendedScreenHandlerType<>(CoolerContainer::new));

    public static final RegistryObject<MenuType<AbstractThermalContainer>> HEATER_CONTAINER
            = CONTAINERS.register("heater_container", () -> new ExtendedScreenHandlerType<>(HeaterContainer::new));

    public static final RegistryObject<MenuType<SewingTableContainer>> SEWING_TABLE_CONTAINER
            = CONTAINERS.register("sewing_table_container", () -> new ExtendedScreenHandlerType<>(SewingTableContainer::new));

    public static void register() {
        CONTAINERS.registerAll();
    }
}
