package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.common.integration.trinkets.TrinketsUtil;

public class WearableTrinketItem extends Item {
    public WearableTrinketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!TrinketsUtil.equipTrinket(player, stack))
            return super.use(level, player, hand);

        level.playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC,
                player.getSoundSource(), 1.0f, 1.0f);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
