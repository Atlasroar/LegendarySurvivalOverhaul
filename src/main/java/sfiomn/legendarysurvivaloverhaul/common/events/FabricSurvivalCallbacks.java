package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class FabricSurvivalCallbacks {
    private FabricSurvivalCallbacks() {
    }

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, target, hitResult) -> {
            if (!world.isClientSide && shouldApplyThirst(player) && target.isAttackable()) {
                ThirstUtil.addExhaustion(player, (float) Config.Baked.onAttackHydrationExhaustion);
                player.causeFoodExhaustion((float) Config.Baked.onAttackFoodExhaustion);
            }
            return InteractionResult.PASS;
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClientSide && shouldApplyThirst(player)
                    && state.getDestroySpeed(world, pos) > 0.0f) {
                ThirstUtil.addExhaustion(player, (float) Config.Baked.onBlockBreakHydrationExhaustion);
            }
        });
    }

    public static void onPlayerJump(Player player) {
        if (!player.level().isClientSide && shouldApplyThirst(player)) {
            ThirstUtil.addExhaustion(player, (float) Config.Baked.onJumpHydrationExhaustion);
        }
    }

    private static boolean shouldApplyThirst(Player player) {
        return !player.isCreative() && !player.isSpectator()
                && Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }
}
