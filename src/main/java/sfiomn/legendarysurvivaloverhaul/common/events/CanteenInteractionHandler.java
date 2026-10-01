package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class CanteenInteractionHandler {

    public static void register() {
        UseBlockCallback.EVENT.register(CanteenInteractionHandler::onRightClickBlock);
    }

    private static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!(itemStack.getItem() instanceof CanteenItem))
            return InteractionResult.PASS;

        return InteractionResult.PASS;
    }
}
