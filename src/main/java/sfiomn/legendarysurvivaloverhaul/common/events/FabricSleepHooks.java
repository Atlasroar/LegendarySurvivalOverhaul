package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public final class FabricSleepHooks {
    private FabricSleepHooks() {
    }

    public static void onSleepFinished(ServerLevel level) {
        for (Player player : level.players()) {
            if (!player.isSleepingLongEnough())
                continue;

            if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.bodyHealthRatioRecoveredFromSleep > 0) {
                for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
                    float healthRecovered = BodyDamageUtil.getMaxHealth(player, bodyPart)
                            * (float) Config.Baked.bodyHealthRatioRecoveredFromSleep;
                    BodyDamageUtil.healBodyPart(player, bodyPart, healthRecovered);
                }
                BodyDamageCapability bodyDamage = CapabilityUtil.getBodyDamageCapability(player);
                bodyDamage.updateBrokenHearts(player);
                BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
            }

            if (Config.Baked.healthRatioRecoveredFromSleep > 0) {
                HealthUtil.updatePlayerMaxHealthAttribute(player);
                float healthRecovered = (float) (player.getMaxHealth() * Config.Baked.healthRatioRecoveredFromSleep);
                player.heal(healthRecovered);
            }
        }
    }
}
