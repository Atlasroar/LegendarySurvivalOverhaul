package sfiomn.legendarysurvivaloverhaul.common.integration.overflowingbars;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.mutable.MutableInt;
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
