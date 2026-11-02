import dev.emi.trinkets.api.TrinketsApi;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.WearableTrinket;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

final class TrinketsAssertions {
    private TrinketsAssertions() {}

    static void checkRegisteredAccessories() {
        for (var item : new net.minecraft.world.item.Item[]{
                ItemRegistry.THERMOMETER.get(), ItemRegistry.NETHER_CHALICE.get(),
                ItemRegistry.SPONGE.get(), ItemRegistry.FIRST_AID_SUPPLIES.get(),
                ItemRegistry.WATER_PURIFIER.get(), ItemRegistry.HEAT_RESISTANCE_RING.get(),
                ItemRegistry.COLD_RESISTANCE_RING.get(), ItemRegistry.THERMAL_RESISTANCE_RING.get()}) {
            if (!(TrinketsApi.getTrinket(item) instanceof WearableTrinket))
                throw new AssertionError("Missing optional accessory adapter for " + item);
        }
    }
}
