package sfiomn.legendarysurvivaloverhaul.common.integration.overflowingbars;

import net.fabricmc.loader.api.FabricLoader;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class OverflowingBarsUtil {
    private static final String LEFT_HEIGHT_KEY = "overflowingbars:leftHeight";
    private static final String RIGHT_HEIGHT_KEY = "overflowingbars:rightHeight";

    public static boolean isHealthBarOverflowing() {
        return LegendarySurvivalOverhaul.overflowingbarsLoaded && rightHeight(0) > 39;
    }

    public static int leftHeight(int fallback) {
        return sharedHeight(LEFT_HEIGHT_KEY, fallback);
    }

    public static int rightHeight(int fallback) {
        return sharedHeight(RIGHT_HEIGHT_KEY, fallback);
    }

    private static int sharedHeight(String key, int fallback) {
        Object value = FabricLoader.getInstance().getObjectShare().get(key);
        return value instanceof Number height ? height.intValue() : fallback;
    }
}
