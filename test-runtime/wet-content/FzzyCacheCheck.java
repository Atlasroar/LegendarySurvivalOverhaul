package sfiomn.legendarysurvivaloverhaul.config.fzzy;

import me.fzzyhmstrs.fzzy_config.event.api.ServerUpdateContext;

import java.util.Map;

public final class FzzyCacheCheck {
    public static void updateFromEditor(int value) throws ReflectiveOperationException {
        var configsField = FzzyConfigBridge.class.getDeclaredField("CONFIGS");
        configsField.setAccessible(true);
        Object adapter = ((Map<?, ?>) configsField.get(null)).get("common");
        if (!(adapter instanceof FzzyCommonConfig config))
            throw new AssertionError("Registered Fzzy Common config missing");
        config.routinePacketSync.validateAndSet(value);
        config.onUpdateServer((ServerUpdateContext) null);
    }

    public static void restoreEditor() {
        FzzyConfigBridge.updateFromData();
    }
}
