package sfiomn.legendarysurvivaloverhaul.common.integration.weathering;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import org.slf4j.LoggerFactory;

public final class WeatheringCompatibility {
    public static final boolean AVAILABLE = available();

    private WeatheringCompatibility() {}

    private static boolean available() {
        var mod = FabricLoader.getInstance().getModContainer("immersive_weathering");
        if (mod.isEmpty())
            return false;
        try {
            if (mod.get().getMetadata().getVersion().compareTo(Version.parse("2.5.0")) >= 0)
                return true;
        } catch (VersionParsingException e) {
            throw new ExceptionInInitializerError(e);
        }
        LoggerFactory.getLogger("legendarysurvivaloverhaul").warn(
                "Immersive Weathering storm integration needs Atlasroar's 2.5.0+ build; found {}. Integration disabled.",
                mod.get().getMetadata().getVersion().getFriendlyString());
        return false;
    }
}
