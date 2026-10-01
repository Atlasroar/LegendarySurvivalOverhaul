package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public final class FabricInteractionCallbacks {
    private FabricInteractionCallbacks() {
    }

    public static void register() {
        UseBlockCallback.EVENT.register(FabricInteractionCallbacks::onUseBlock);
    }

    private static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        if (shouldApplyThirst(player) && hand == InteractionHand.MAIN_HAND && player.getMainHandItem().isEmpty()) {
            ThirstCapability thirst = CapabilityUtil.getThirstCapability(player);
            if (!thirst.isHydrationLevelAtMax()) {
                if (level.getBlockEntity(hit.getBlockPos()) instanceof MenuProvider)
                    return InteractionResult.PASS;

                JsonThirstBlock thirstBlock = ThirstUtil.getBlockThirstLookedAt(player, 3.0);
                JsonThirstBlock thirstFluid = ThirstUtil.getFluidThirstLookedAt(player, 3.0);

                if (hasHydration(thirstBlock) && !player.isCrouching()) {
                    drinkAtBlock(player, thirstBlock, level);
                    return InteractionResult.CONSUME;
                }

                if (hasHydration(thirstFluid))
                    drinkAtBlock(player, thirstFluid, level);
            }
        }

        if (!level.isClientSide && LegendarySurvivalOverhaul.sereneSeasonsLoaded
                && hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer
                && player.getMainHandItem().is(Items.BONE_MEAL)
                && !SereneSeasonsUtil.plantCanGrow(
                        level, hit.getBlockPos(), level.getBlockState(hit.getBlockPos()))) {
            player.displayClientMessage(Component.translatable(
                    "message." + LegendarySurvivalOverhaul.MOD_ID + ".bonemeal.not_correct_season"), true);
        }

        if (Config.Baked.temperatureEnabled && hand == InteractionHand.MAIN_HAND && !level.isClientSide) {
            BlockState usedBlock = level.getBlockState(hit.getBlockPos());
            TemperatureUtil.applyConsumableBlockTemperature(player, usedBlock);
        }

        return InteractionResult.PASS;
    }

    private static boolean shouldApplyThirst(Player player) {
        return !player.isCreative() && !player.isSpectator() && Config.Baked.thirstEnabled && ThirstUtil.isThirstActive(player);
    }

    private static boolean hasHydration(JsonThirstBlock thirst) {
        return thirst != null && (thirst.hydration != 0 || thirst.saturation != 0);
    }

    private static void drinkAtBlock(Player player, JsonThirstBlock thirst, Level level) {
        if (level.isClientSide)
            playerDrinkEffect(player);
        else
            ThirstUtil.takeDrink(player, thirst.hydration, thirst.saturation, thirst.effects);
    }

    private static void playerDrinkEffect(Player player) {
        player.swing(InteractionHand.MAIN_HAND);
        player.playSound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK, 1.0f, 1.0f);
    }
}
