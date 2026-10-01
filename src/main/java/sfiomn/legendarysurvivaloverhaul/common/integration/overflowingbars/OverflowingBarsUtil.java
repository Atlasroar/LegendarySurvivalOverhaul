package sfiomn.legendarysurvivaloverhaul.common.integration.overflowingbars;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.mutable.MutableInt;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class OverflowingBarsUtil {
    private static final String LEFT_HEIGHT_KEY = "overflowingbars:leftHeight";
    private static final String RIGHT_HEIGHT_KEY = "overflowingbars:rightHeight";

    // Overflowing Bars always reserves one row of height for a mount/vehicle health bar, even when
    // the player isn't riding anything that shows one. Its own math is
    // Math.max(1, getVisibleVehicleHeartRows(vehicleMaxHearts) - 1) * 10, and with no vehicle that
    // evaluates to Math.max(1, -1) * 10 == 10 rather than 0, leaving a permanent empty gap between
    // the armor/toughness row and anything stacked above it (like our thirst bar).
    private static final int VEHICLE_ROW_QUIRK_HEIGHT = 10;

    public static boolean isHealthBarOverflowing() {
        return LegendarySurvivalOverhaul.overflowingbarsLoaded && rightHeight(0) > 39;
    }

    public static int leftHeight(int fallback) {
        return sharedHeight(LEFT_HEIGHT_KEY, fallback);
    }

    /**
     * Overflowing Bars only publishes its "overflowingbars:leftHeight" object-share key when the
     * toughness bar is explicitly configured to render on the left side (off by default). With its
     * default settings it still draws an armor row above the health bar without ever reporting the
     * extra height, so relying on the shared key alone causes our shield-heart row to overlap that
     * armor row. This replicates Overflowing Bars' own default-config height math (health layering +
     * armor row) purely from public player state, so we have a sane fallback when the key is absent.
     */
    public static int estimateHealthBarLeftHeight(Player player) {
        boolean twoHealthRows = player.getAbsorptionAmount() > 0.0F
                && player.getMaxHealth() + player.getAbsorptionAmount() > 20.0F;
        int height = 39 + (twoHealthRows ? 20 : 10);
        if (player.getArmorValue() > 0)
            height += 10;
        return height;
    }

    public static int rightHeight(int fallback) {
        return sharedHeight(RIGHT_HEIGHT_KEY, fallback);
    }

    /**
     * Corrects the shared right-height value for the vehicle row quirk described above, so our
     * thirst bar (and anything reserving height after it) sits directly above the real content
     * instead of leaving a phantom empty row. Call once per frame before reading rightHeight.
     */
    public static void correctVehicleRowQuirk(Player player) {
        if (!LegendarySurvivalOverhaul.overflowingbarsLoaded)
            return;

        Entity vehicle = player.getVehicle();
        boolean hasVehicleHealthBar = vehicle instanceof LivingEntity livingVehicle && livingVehicle.showVehicleHealth();
        if (hasVehicleHealthBar)
            return;

        Object value = FabricLoader.getInstance().getObjectShare().get(RIGHT_HEIGHT_KEY);
        if (value instanceof MutableInt height && height.intValue() >= 39 + VEHICLE_ROW_QUIRK_HEIGHT)
            height.subtract(VEHICLE_ROW_QUIRK_HEIGHT);
    }

    public static void reserveLeftHeight(int height) {
        reserveHeight(LEFT_HEIGHT_KEY, height);
    }

    public static void reserveRightHeight(int height) {
        reserveHeight(RIGHT_HEIGHT_KEY, height);
    }

    private static int sharedHeight(String key, int fallback) {
        Object value = FabricLoader.getInstance().getObjectShare().get(key);
        return value instanceof MutableInt height ? height.intValue() : fallback;
    }

    private static void reserveHeight(String key, int height) {
        Object value = FabricLoader.getInstance().getObjectShare().get(key);
        if (value instanceof MutableInt sharedHeight)
            sharedHeight.add(height);
    }
}
