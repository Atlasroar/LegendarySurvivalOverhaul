package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FabricDamageHooks {
    private FabricDamageHooks() {
    }

    public static void onPlayerActuallyHurt(Player player, DamageSource source, float damage) {
        if (player.level().isClientSide || player.isCreative() || player.isSpectator()
                || !Config.Baked.localizedBodyDamageEnabled)
            return;

        float bodyPartDamage = damage * (float) Config.Baked.bodyDamageMultiplier;
        JsonBodyPartsDamageSource configuredSource = BodyDamageDataManager.getBodyParts(source.getMsgId());
        List<BodyPartEnum> hitBodyParts = new ArrayList<>();
        if (configuredSource != null) {
            if (configuredSource.damageDistribution != DamageDistributionEnum.NONE)
                hitBodyParts.addAll(configuredSource.getBodyParts(player));
        } else if (source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() != null) {
            hitBodyParts.addAll(PlayerModelUtil.getPreciseEntityImpact(source.getDirectEntity(), player));
        } else if (source.getDirectEntity() != null) {
            List<BodyPartEnum> possibleHitParts = PlayerModelUtil.getEntityImpact(source.getDirectEntity(), player);
            if (!possibleHitParts.isEmpty())
                hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, possibleHitParts));
        }

        if (configuredSource == null && hitBodyParts.isEmpty())
            hitBodyParts.addAll(DamageDistributionEnum.ONE_OF.getBodyParts(player, Arrays.asList(BodyPartEnum.values())));

        if (!hitBodyParts.isEmpty())
            BodyDamageUtil.balancedHurtBodyParts(player, hitBodyParts, bodyPartDamage);
    }
}
