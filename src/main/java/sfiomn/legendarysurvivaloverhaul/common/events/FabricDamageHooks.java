package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.ModDamageTypes;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.DamageDistributionEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.PlayerModelUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FabricDamageHooks {
    private FabricDamageHooks() {
    }

    public static float modifyIncomingDamage(LivingEntity entity, DamageSource source, float damage) {
        if (entity.hasEffect(MobEffectRegistry.VULNERABILITY.get())
                && !source.is(DamageTypes.FALL)
                && !source.is(DamageTypes.STARVE)
                && !source.is(DamageTypes.FREEZE)
                && !source.is(DamageTypes.DROWN)
                && !source.is(ModDamageTypes.DEHYDRATION)
                && !source.is(ModDamageTypes.HYPOTHERMIA)
                && !source.is(ModDamageTypes.HYPERTHERMIA)) {
            int amplifier = entity.getEffect(MobEffectRegistry.VULNERABILITY.get()).getAmplifier();
            return damage * (2.0f + 0.2f * amplifier);
        }

        if (source.is(DamageTypes.FALL) && entity.hasEffect(MobEffectRegistry.HARD_FALLING.get())) {
            int amplifier = entity.getEffect(MobEffectRegistry.HARD_FALLING.get()).getAmplifier();
            entity.level().playSound(null, entity, SoundRegistry.HARD_FALLING_HURT.get(),
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            return damage * (2.0f + 0.2f * amplifier);
        }

        return damage;
    }

    public static float onPlayerActuallyHurt(Player player, DamageSource source, float damage) {
        if (player.level().isClientSide || player.isCreative() || player.isSpectator()
                || !Config.Baked.localizedBodyDamageEnabled || damage <= 0)
            return damage;

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

        if (source.is(DamageTypeTags.IS_PROJECTILE)
                && hitBodyParts.contains(BodyPartEnum.HEAD)
                && Config.Baked.headCriticalShotMultiplier > 1
                && player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            player.level().playLocalSound(player.blockPosition(), SoundRegistry.HEADSHOT.get(),
                    SoundSource.HOSTILE, 1.0F, 1.0F, false);
            return damage * (float) Config.Baked.headCriticalShotMultiplier;
        }
        return damage;
    }
}
