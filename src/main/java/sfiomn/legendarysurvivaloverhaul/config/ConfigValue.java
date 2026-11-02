package sfiomn.legendarysurvivaloverhaul.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * The dependency-free setting schema. Both file loading and the optional Fzzy editor
 * use these defaults and constraints.
 */
public abstract class ConfigValue<T> implements Supplier<T> {
    private T value;
    public final T defaultValue;

    protected ConfigValue(T value) {
        this.value = value;
        this.defaultValue = value;
    }

    @Override
    public final T get() {
        return value;
    }

    public final void validateAndSet(T value) {
        this.value = validate(value);
    }

    protected abstract T validate(T value);

    public ValidatedList<T> toList(List<T> values) {
        return new ValidatedList<>(values, this);
    }

    public static final class ValidatedBoolean extends ConfigValue<Boolean> {
        public ValidatedBoolean(boolean value) { super(value); }
        protected Boolean validate(Boolean value) { return java.util.Objects.requireNonNull(value); }
    }

    public static final class ValidatedEnum<E extends Enum<E>> extends ConfigValue<E> {
        public ValidatedEnum(E value) { super(value); }
        protected E validate(E value) { return java.util.Objects.requireNonNull(value); }
    }

    public static final class ValidatedString extends ConfigValue<String> {
        public final String regex;
        public ValidatedString(String value) { this(value, null); }
        public ValidatedString(String value, String regex) {
            super(value);
            this.regex = regex;
        }
        protected String validate(String value) {
            if (value == null || regex != null && !Pattern.matches(regex, value))
                throw new IllegalArgumentException("Invalid config string: " + value);
            return value;
        }
    }

    public abstract static class ValidatedNumber<T extends Number> extends ConfigValue<T> {
        public enum WidgetType { TEXTBOX, TEXTBOX_WITH_BUTTONS, SLIDER }
        public final T max, min;
        public final WidgetType widget;
        protected ValidatedNumber(T value, T max, T min, WidgetType widget) {
            super(value);
            this.max = max;
            this.min = min;
            this.widget = widget;
        }
    }

    public static final class ValidatedInt extends ValidatedNumber<Integer> {
        public ValidatedInt(int value, int max, int min) {
            this(value, max, min, max == Integer.MAX_VALUE || min == Integer.MIN_VALUE ? WidgetType.TEXTBOX : WidgetType.SLIDER);
        }
        public ValidatedInt(int value, int max, int min, WidgetType widget) { super(value, max, min, widget); }
        protected Integer validate(Integer value) { return Math.max(min, Math.min(max, value)); }
    }

    public static final class ValidatedDouble extends ValidatedNumber<Double> {
        public ValidatedDouble(double value, double max, double min) {
            this(value, max, min, max == Double.MAX_VALUE || min == -Double.MAX_VALUE ? WidgetType.TEXTBOX : WidgetType.SLIDER);
        }
        public ValidatedDouble(double value, double max, double min, WidgetType widget) { super(value, max, min, widget); }
        protected Double validate(Double value) {
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Non-finite config number");
            return Math.max(min, Math.min(max, value));
        }
    }

    public static final class ValidatedList<E> extends ConfigValue<List<E>> {
        public final ConfigValue<E> element;
        public ValidatedList(List<E> values, ConfigValue<E> element) {
            super(List.copyOf(values));
            this.element = element;
        }
        protected List<E> validate(List<E> values) {
            List<E> result = new ArrayList<>();
            for (E value : values) result.add(element.validate(value));
            return List.copyOf(result);
        }
        public static ValidatedList<String> ofString(List<String> values) {
            return new ValidatedString("").toList(values);
        }
    }
}
