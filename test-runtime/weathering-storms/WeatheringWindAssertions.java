import com.ordana.immersive_weathering.reg.ModEntities;
import com.ordana.immersive_weathering.wind.FreezingWindEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

final class WeatheringWindAssertions {
    static void hit(ServerLevel level, ServerPlayer player) {
        level.addNewPlayer(player);
        FreezingWindEntity wind = ModEntities.FREEZING_WIND.get().create(level);
        if (wind == null) throw new AssertionError("Wind entity factory");
        wind.setPos(player.position());
        wind.tickCount = 5;
        wind.tick();
    }
}
