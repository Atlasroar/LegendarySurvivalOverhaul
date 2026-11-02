package sfiomn.legendarysurvivaloverhaul.config;

import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedList;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedBoolean;
import sfiomn.legendarysurvivaloverhaul.config.ConfigValue.ValidatedString;

import java.util.List;

@SuppressWarnings("unused")
public class TrinketsConfig extends ConfigData {
    public TrinketsConfig() {
        super("trinkets");
    }

    public ValidatedBoolean useConfiguredSlots = new ValidatedBoolean(true);

    public ValidatedList<String> thermometerSlots = slots("legs/belt");

    public ValidatedList<String> netherChaliceSlots = slots("chest/necklace");

    public ValidatedList<String> spongeSlots = slots("chest/back");

    public ValidatedList<String> heatResistanceRingSlots = slots("hand/ring");

    public ValidatedList<String> coldResistanceRingSlots = slots("hand/ring");

    public ValidatedList<String> thermalResistanceRingSlots = slots("hand/ring");

    public ValidatedList<String> firstAidSuppliesSlots = slots("hand/glove");

    public ValidatedList<String> waterPurifierSlots = slots("head/face");

    private static ValidatedList<String> slots(String defaultSlot) {
        return new ValidatedString(defaultSlot, "[a-z0-9_.-]+/[a-z0-9_.-]+").toList(List.of(defaultSlot));
    }
}
