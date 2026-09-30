package sfiomn.legendarysurvivaloverhaul.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.CanteenProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarSeasonTypeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.SeasonalCalendarTimeProperty;
import sfiomn.legendarysurvivaloverhaul.client.itemproperties.ThermometerProperty;
import sfiomn.legendarysurvivaloverhaul.client.events.ClientModBusEvents;
import sfiomn.legendarysurvivaloverhaul.client.events.FabricClientCallbacks;
import sfiomn.legendarysurvivaloverhaul.client.screens.SewingTableScreen;
import sfiomn.legendarysurvivaloverhaul.client.screens.ThermalScreen;
import sfiomn.legendarysurvivaloverhaul.common.TickPhase;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.ModCapabilities;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ContainerRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.KeyMappingRegistry;

public final class LegendarySurvivalOverhaulClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		Config.Baked.bakeClient();
		ClientModBusEvents.register();
		FabricClientCallbacks.register();
		KeyMappingRegistry.register();
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (client.player != null)
				ModCapabilities.onPlayerTick(client.player, TickPhase.START);
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null)
				ModCapabilities.onPlayerTick(client.player, TickPhase.END);
		});

		MenuScreens.register(ContainerRegistry.COOLER_CONTAINER.get(), ThermalScreen::new);
		MenuScreens.register(ContainerRegistry.HEATER_CONTAINER.get(), ThermalScreen::new);
		MenuScreens.register(ContainerRegistry.SEWING_TABLE_CONTAINER.get(), SewingTableScreen::new);

		ItemProperties.register(ItemRegistry.THERMOMETER.get(), id("temperature"), new ThermometerProperty());
		ItemProperties.register(ItemRegistry.CANTEEN.get(), id("thirstenum"), new CanteenProperty());
		ItemProperties.register(ItemRegistry.LARGE_CANTEEN.get(), id("thirstenum"), new CanteenProperty());
		if (LegendarySurvivalOverhaul.sereneSeasonsLoaded || LegendarySurvivalOverhaul.eclipticSeasonsLoaded) {
			ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), id("time"), new SeasonalCalendarTimeProperty());
			ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), id("seasontype"), new SeasonalCalendarSeasonTypeProperty());
		}
	}

	private static ResourceLocation id(String path)
	{
		return new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, path);
	}
}
