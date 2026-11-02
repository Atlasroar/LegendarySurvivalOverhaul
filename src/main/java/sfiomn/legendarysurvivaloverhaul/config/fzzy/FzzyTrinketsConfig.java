package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import sfiomn.legendarysurvivaloverhaul.config.*;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

@SuppressWarnings("unused")
public class FzzyTrinketsConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
    public FzzyTrinketsConfig() {
        super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "trinkets"));
    }

    @Override
    public void onSyncClient() {
        FzzyConfigBridge.bake(this, "trinkets");
    }

    @Override
    public void onSyncServer() {
        FzzyConfigBridge.bake(this, "trinkets");
    }

    @Override
    public void onUpdateClient() {
        FzzyConfigBridge.bake(this, "trinkets");
    }

    @Override
    public void onUpdateServer(ServerUpdateContext context) {
        FzzyConfigBridge.bake(this, "trinkets");
    }

    public ConfigGroup group_slots = new ConfigGroup("group_slots");

    @Comment("Use the allowed-slot lists below for LSO accessories instead of their item slot tags. Disable to use Trinkets/datapack slot tags unchanged. The Respirator is excluded. Slots must already exist for the player; these lists do not create slots. Changes affect new insertions, not already equipped items.")
    public ValidatedBoolean useConfiguredSlots = FzzyConfigBridge.field("trinkets", "useConfiguredSlots");

    @Comment("Allowed Thermometer slots as group/slot IDs. Default legs/belt. Multiple entries allow multiple locations; an empty list disables new equipping. Existing custom slots from mods/datapacks are supported.")
    public ValidatedList<String> thermometerSlots = FzzyConfigBridge.field("trinkets", "thermometerSlots");
    @Comment("Allowed Nether Chalice slots as group/slot IDs. Default chest/necklace. Empty disables new equipping.")
    public ValidatedList<String> netherChaliceSlots = FzzyConfigBridge.field("trinkets", "netherChaliceSlots");
    @Comment("Allowed Sponge slots as group/slot IDs. Default chest/back. Empty disables new equipping.")
    public ValidatedList<String> spongeSlots = FzzyConfigBridge.field("trinkets", "spongeSlots");
    @Comment("Allowed Heat Resistance Ring slots as group/slot IDs. Default hand/ring. Empty disables new equipping.")
    public ValidatedList<String> heatResistanceRingSlots = FzzyConfigBridge.field("trinkets", "heatResistanceRingSlots");
    @Comment("Allowed Cold Resistance Ring slots as group/slot IDs. Default hand/ring. Empty disables new equipping.")
    public ValidatedList<String> coldResistanceRingSlots = FzzyConfigBridge.field("trinkets", "coldResistanceRingSlots");
    @Comment("Allowed Thermal Resistance Ring slots as group/slot IDs. Default hand/ring. Empty disables new equipping.")
    public ValidatedList<String> thermalResistanceRingSlots = FzzyConfigBridge.field("trinkets", "thermalResistanceRingSlots");
    @Comment("Allowed First Aid Supplies slots as group/slot IDs. Default hand/glove. Empty disables new equipping.")
    public ValidatedList<String> firstAidSuppliesSlots = FzzyConfigBridge.field("trinkets", "firstAidSuppliesSlots");
    @Comment("Allowed Water Purifier slots as group/slot IDs. Default head/face. The separate Thin Air Respirator remains unchanged. Empty disables new equipping.")
    @ConfigGroup.Pop
    public ValidatedList<String> waterPurifierSlots = FzzyConfigBridge.field("trinkets", "waterPurifierSlots");

}
