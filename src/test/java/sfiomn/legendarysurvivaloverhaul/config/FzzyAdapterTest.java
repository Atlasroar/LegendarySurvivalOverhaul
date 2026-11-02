package sfiomn.legendarysurvivaloverhaul.config;

import java.lang.reflect.Field;
import java.util.Map;

/** Ensures every optional editor binding is backed by exactly the dependency-free schema. */
public final class FzzyAdapterTest {
    public static void main(String[] args) throws Exception {
        net.minecraft.SharedConstants.tryDetectVersion();
        net.minecraft.server.Bootstrap.bootStrap();
        Config.CLIENT = new ClientConfig();
        Config.COMMON = new CommonConfig();
        Config.AIR = new AirConfig();
        Config.TEMPERATURE = new TemperatureConfig();
        Config.SEASONS = new SeasonsConfig();
        Config.THIRST = new ThirstConfig();
        Config.HEALTH = new HealthConfig();
        Config.BODY_DAMAGE = new BodyDamageConfig();
        Config.TRINKETS = new TrinketsConfig();
        int checked = 0;
        for (ConfigData schema : Config.all()) {
            Class<?> type = Class.forName("sfiomn.legendarysurvivaloverhaul.config.fzzy.Fzzy" + schema.getClass().getSimpleName());
            Object adapter = type.getConstructor().newInstance();
            Map<String, ConfigValue<?>> settings = schema.fields();
            int count = 0;
            for (Field field : type.getFields()) {
                if (!field.getType().getSimpleName().startsWith("Validated")) continue;
                count++;
                ConfigValue<?> setting = settings.get(field.getName());
                if (setting == null) throw new AssertionError("Unbound editor field " + field);
                Object value = field.get(adapter);
                Object actual = value.getClass().getMethod("get").invoke(value);
                if (!setting.get().equals(actual)) throw new AssertionError("Default mismatch " + field);
                if (setting instanceof ConfigValue.ValidatedNumber<?> number) {
                    Object high = Double.MAX_VALUE;
                    if (number instanceof ConfigValue.ValidatedInt) high = Integer.MAX_VALUE;
                    value.getClass().getMethod("validateAndSet", Object.class).invoke(value, high);
                    actual = value.getClass().getMethod("get").invoke(value);
                    FileConfigStore.set(setting, high);
                    if (!setting.get().equals(actual)) throw new AssertionError("Validation mismatch " + field);
                }
                checked++;
            }
            if (count != settings.size()) throw new AssertionError("Missing editor fields for " + schema.name);
        }
        System.out.println("PASS: " + checked + " Fzzy editor bindings share schema defaults and numeric validators");
    }
}
