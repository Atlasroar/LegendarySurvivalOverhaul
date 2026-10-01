package sfiomn.legendarysurvivaloverhaul.mixin;

import glitchcore.event.player.PlayerInteractEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;

@Pseudo
@Mixin(targets = "sereneseasons.season.SeasonalCropGrowthHandler", remap = false)
public abstract class SereneSeasonsBonemealMixin {
    @Inject(method = "applyBonemeal", at = @At("HEAD"), remap = false)
    private static void legendarysurvivaloverhaul$showOutOfSeasonWarning(
            PlayerInteractEvent.UseBlock event, CallbackInfo callback) {
        if (event.getHand() != InteractionHand.MAIN_HAND || !event.getItemStack().is(Items.BONE_MEAL))
            return;

        var player = event.getPlayer();
        var level = player.level();
        var pos = event.getHitResult().getBlockPos();
        if (!SereneSeasonsUtil.plantCanGrow(level, pos, level.getBlockState(pos))) {
            player.displayClientMessage(Component.translatable(
                    "message." + LegendarySurvivalOverhaul.MOD_ID + ".bonemeal.not_correct_season"), true);
        }
    }
}
