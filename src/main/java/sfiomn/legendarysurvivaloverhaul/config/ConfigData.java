package sfiomn.legendarysurvivaloverhaul.config;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/** A named group of settings with no optional-mod types in its API. */
public abstract class ConfigData {
    public final String name;

    protected ConfigData(String name) {
        this.name = name;
    }

    public final Map<String, ConfigValue<?>> fields() {
        Map<String, ConfigValue<?>> fields = new LinkedHashMap<>();
        for (Field field : getClass().getFields()) {
            if (!ConfigValue.class.isAssignableFrom(field.getType())) continue;
            try {
                fields.put(field.getName(), (ConfigValue<?>) field.get(this));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return fields;
    }

    public final void save() {
        FileConfigStore.save(this);
    }
}
