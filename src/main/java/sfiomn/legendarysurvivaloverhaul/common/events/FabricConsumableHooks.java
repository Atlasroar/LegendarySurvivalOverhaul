package sfiomn.legendarysurvivaloverhaul.common.events;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;

public final class FabricConsumableHooks {
    private FabricConsumableHooks() {
    }

    public static void onFinishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!(entity instanceof Player player))
            return;

        if (!level.isClientSide) {
            var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            TemperatureUtil.applyConsumableTemperature(player, itemId);
            ThirstUtil.takeDrink(player, stack);
        }

        if (!(stack.getItem() instanceof BodyHealingItem))
            BodyDamageUtil.applyConsumableHealing(player, stack, true);
    }
}
