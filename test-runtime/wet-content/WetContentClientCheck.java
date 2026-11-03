import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

public final class WetContentClientCheck implements ClientModInitializer {
    private boolean checked;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (checked || client.getOverlay() != null || client.screen == null)
                return;
            checked = true;
            try {
                var missing = client.getModelManager().getMissingModel();
                for (Item item : new Item[] {ItemRegistry.ICE_SKATES.get(), ItemRegistry.SNOW_HELMET.get(),
                        ItemRegistry.SNOW_CHEST.get(), ItemRegistry.SNOW_LEGGINGS.get(), ItemRegistry.SNOW_BOOTS.get(),
                        BlockRegistry.CRIMSON_LILY.get().asItem(), BlockRegistry.WARPED_LILY.get().asItem()}) {
                    var model = client.getItemRenderer().getModel(new ItemStack(item), null, null, 0);
                    WetContentCheck.check(model != missing, "Item model baked: " + item);
                    WetContentCheck.check(!model.getParticleIcon().contents().name().getPath().equals("missingno"),
                            "Item texture loaded: " + item);
                }
                for (String layer : new String[] {"blades", "boots"}) {
                    var root = client.getEntityModels().bakeLayer(new ModelLayerLocation(
                            new ResourceLocation("legendarysurvivaloverhaul", "ice_skates"), layer));
                    WetContentCheck.check(!root.getChild("left_leg").isEmpty(), "Left skate model: " + layer);
                    WetContentCheck.check(!root.getChild("right_leg").isEmpty(), "Right skate model: " + layer);
                }
                WetContentCheck.check(I18n.get("item.legendarysurvivaloverhaul.snow_helmet").equals("Fur Cap"),
                        "Existing snow ID displays fur replacement name");
                System.out.println("WET_CONTENT_CLIENT_CHECK_PASSED");
            } finally {
                client.stop();
            }
        });
    }
}
