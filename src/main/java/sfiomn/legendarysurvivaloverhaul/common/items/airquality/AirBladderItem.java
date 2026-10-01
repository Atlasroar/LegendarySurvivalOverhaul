package sfiomn.legendarysurvivaloverhaul.common.items.airquality;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;

/**
 * A refillable air tank: refills from a green/blue air source while damaged, or spends its remaining durability to
 * refill the wearer's air supply while in bad air. Used for both the Air Bladder and Reinforced Air Bladder (which
 * share this class and only differ in starting durability). Adapted from Fuzss' MIT-licensed "Thin Air" mod
 * (https://github.com/Fuzss/thinair).
 */
public class AirBladderItem extends Item implements FabricItem {

    public AirBladderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        AirQualityLevel airQualityLevel = AirQualityUtil.getAirQualityAtLocation(player);
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (airQualityLevel.canRefillAir && itemInHand.isDamaged() ||
                !airQualityLevel.canRefillAir && itemInHand.getDamageValue() < itemInHand.getMaxDamage() && player.getAirSupply() < player.getMaxAirSupply()) {
            return ItemUtils.startUsingInstantly(level, player, interactionHand);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseDuration) {
        boolean stopUsing = true;
        AirQualityLevel airQualityLevel = AirQualityUtil.getAirQualityAtLocation(entity);
        if (airQualityLevel.canRefillAir) {
            if (itemStack.isDamaged()) {
                itemStack.setDamageValue(itemStack.getDamageValue() - 4);
                stopUsing = false;
            }
        } else if (itemStack.getDamageValue() < itemStack.getMaxDamage()) {
            int i = 4;
            while (i-- > 0 && entity.getAirSupply() < entity.getMaxAirSupply()) {
                entity.setAirSupply(entity.getAirSupply() + 1);
                itemStack.hurt(1, entity.getRandom(), entity instanceof ServerPlayer player ? player : null);
                stopUsing = false;
            }
        }
        if (!stopUsing) {
            if (remainingUseDuration <= this.getUseDuration(itemStack) - 7 && remainingUseDuration % 4 == 0) {
                entity.playSound(this.getDrinkingSound(), 0.5F, entity.level().random.nextFloat() * 0.1F + 0.9F);
            }
        } else {
            entity.releaseUsingItem();
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player && player.getAirSupply() >= player.getMaxAirSupply()) {
            if (!AirQualityUtil.getAirQualityAtLocation(livingEntity).canRefillAir) {
                player.getCooldowns().addCooldown(this, 150);
            }
        }
        super.releaseUsing(itemStack, level, livingEntity, timeCharged);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }
}
