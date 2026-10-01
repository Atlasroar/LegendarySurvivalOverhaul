package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"))
    private void legendarysurvivaloverhaul$showOutOfSeasonWarning(
            UseOnContext context, CallbackInfoReturnable<InteractionResult> callback) {
        if (!context.getLevel().isClientSide || !LegendarySurvivalOverhaul.sereneSeasonsLoaded
                || context.getPlayer() == null)
            return;

        if (!SereneSeasonsUtil.plantCanGrow(
                context.getLevel(), context.getClickedPos(),
                context.getLevel().getBlockState(context.getClickedPos()))) {
            context.getPlayer().displayClientMessage(Component.translatable(
                    "message." + LegendarySurvivalOverhaul.MOD_ID + ".bonemeal.not_correct_season"), true);
        }
    }
}
