package sfiomn.legendarysurvivaloverhaul.api.data.json;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;

import java.util.Collections;
import java.util.List;

/**
 * Data-driven air quality for a dimension, with optional height-bounded overrides (e.g. thin, yellow air above the
 * build limit, or in deep caves, while the rest of the dimension is normal, green air). Adapted from Fuzss'
 * MIT-licensed "Thin Air" mod (https://github.com/Fuzss/thinair).
 */
public class JsonAirQualityDimension {
    public static final Codec<JsonAirQualityDimension> CODEC = RecordCodecBuilder.<JsonAirQualityDimension>create(instance -> instance.group(
            AirQualityLevel.CODEC.optionalFieldOf("base_quality", AirQualityLevel.GREEN).forGetter(d -> d.baseQuality),
            BoundedAirQuality.CODEC.listOf().optionalFieldOf("bounded_qualities", Collections.emptyList()).forGetter(d -> d.boundedQualities)
    ).apply(instance, JsonAirQualityDimension::new));

    public final AirQualityLevel baseQuality;
    public final List<BoundedAirQuality> boundedQualities;

    public JsonAirQualityDimension(AirQualityLevel baseQuality, List<BoundedAirQuality> boundedQualities) {
        this.baseQuality = baseQuality;
        this.boundedQualities = boundedQualities;
    }

    public AirQualityLevel getAirQualityAtHeight(int height) {
        for (BoundedAirQuality bound : this.boundedQualities) {
            if (bound.containsValue(height)) {
                return bound.quality;
            }
        }
        return this.baseQuality;
    }

    public static class BoundedAirQuality {
        public static final Codec<BoundedAirQuality> CODEC = RecordCodecBuilder.<BoundedAirQuality>create(instance -> instance.group(
                AirQualityLevel.CODEC.fieldOf("quality").forGetter(b -> b.quality),
                Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(b -> b.min),
                Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(b -> b.max)
        ).apply(instance, BoundedAirQuality::new)).flatXmap(BoundedAirQuality::validate, BoundedAirQuality::validate);

        public final AirQualityLevel quality;
        public final int min;
        public final int max;

        public BoundedAirQuality(AirQualityLevel quality, int min, int max) {
            this.quality = quality;
            this.min = min;
            this.max = max;
        }

        private static DataResult<BoundedAirQuality> validate(BoundedAirQuality value) {
            return value.max <= value.min ?
                    DataResult.error(() -> "Max must be larger than min, min: " + value.min + ", max: " + value.max) :
                    DataResult.success(value);
        }

        public boolean containsValue(int value) {
            return value >= this.min && value < this.max;
        }
    }
}
