package lso.check;

import net.minecraft.world.entity.Entity;

public final class SkateLookupCheck {
    private static Entity watched;
    private static int supportQueries;

    public static void begin(Entity entity) {
        watched = entity;
        supportQueries = 0;
    }

    public static void record(Entity entity) {
        if (entity == watched) supportQueries++;
    }

    public static int finish() {
        watched = null;
        return supportQueries;
    }
}
