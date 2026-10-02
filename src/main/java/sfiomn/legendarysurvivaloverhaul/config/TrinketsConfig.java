package sfiomn.legendarysurvivaloverhaul.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.List;

@SuppressWarnings("unused")
public class TrinketsConfig extends me.fzzyhmstrs.fzzy_config.config.Config {
    public TrinketsConfig() {
        super(new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "trinkets"));
    }

    @Override
    public void onSyncClient() {
        Config.bake(this);
    }

    @Override
    public void onSyncServer() {
        Config.bake(this);
    }

    @Override
    public void onUpdateClient() {
        Config.bake(this);
    }

    @Override
    public void onUpdateServer(ServerUpdateContext context) {
        Config.bake(this);
    }

    public ConfigGroup group_slots = new ConfigGroup("group_slots");

    @Comment("Use the allowed-slot lists below for LSO accessories instead of their item slot tags. Disable to use Trinkets/datapack slot tags unchanged. The Respirator is excluded. Slots must already exist for the player; these lists do not create slots. Changes affect new insertions, not already equipped items.")
    public ValidatedBoolean useConfiguredSlots = new ValidatedBoolean(true);

    @Comment("Allowed Thermometer slots as group/slot IDs. Default legs/belt. Multiple entries allow multiple locations; an empty list disables new equipping. Existing custom slots from mods/datapacks are supported.")
    public ValidatedList<String> thermometerSlots = slots("legs/belt");
    @Comment("Allowed Nether Chalice slots as group/slot IDs. Default chest/necklace. Empty disables new equipping.")
    public ValidatedList<String> netherChaliceSlots = slots("chest/necklace");
    @Comment("Allowed Sponge slots as group/slot IDs. Default chest/back. Empty disables new equipping.")
    public ValidatedList<String> spongeSlots = slots("chest/back");
    @Comment("Allowed Heat Resistance Ring slots as group/slot IDs. Default hand/ring. Empty disables new equipping.")
    public ValidatedList<String> heatResistanceRingSlots = slots("hand/ring");
    @Comment("Allowed Cold Resistance Ring slots as group/slot IDs. Default hand/ring. Empty disables new equipping.")
    public ValidatedList<String> coldResistanceRingSlots = slots("hand/ring");
    @Comment("Allowed Thermal Resistance Ring slots as group/slot IDs. Default hand/ring. Empty disables new equipping.")
    public ValidatedList<String> thermalResistanceRingSlots = slots("hand/ring");
    @Comment("Allowed First Aid Supplies slots as group/slot IDs. Default hand/glove. Empty disables new equipping.")
    public ValidatedList<String> firstAidSuppliesSlots = slots("hand/glove");
    @Comment("Allowed Water Purifier slots as group/slot IDs. Default head/face. The separate Thin Air Respirator remains unchanged. Empty disables new equipping.")
    @ConfigGroup.Pop
    public ValidatedList<String> waterPurifierSlots = slots("head/face");

    private static ValidatedList<String> slots(String defaultSlot) {
        return new ValidatedString(defaultSlot, "[a-z0-9_.-]+/[a-z0-9_.-]+").toList(List.of(defaultSlot));
    }
}
