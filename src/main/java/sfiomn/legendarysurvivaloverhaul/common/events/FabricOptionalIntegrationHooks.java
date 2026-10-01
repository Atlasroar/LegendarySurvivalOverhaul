package sfiomn.legendarysurvivaloverhaul.common.events;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.integration.medsandherbs.MedsAndHerbsUtil;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

public final class FabricOptionalIntegrationHooks {
    private static final ResourceLocation MORPHINE_SYRINGE = new ResourceLocation("meds_and_herbs", "syringe_morphine");

    private FabricOptionalIntegrationHooks() {
    }

    public static void register() {
        UseItemCallback.EVENT.register((player, level, hand) -> {
            if (!LegendarySurvivalOverhaul.medsandherbsLoaded)
                return InteractionResultHolder.pass(player.getItemInHand(hand));

            ItemStack stack = player.getItemInHand(hand);
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (!MORPHINE_SYRINGE.equals(itemId))
                return InteractionResultHolder.pass(stack);

            if (player.hasEffect(MobEffectRegistry.PAINKILLER_ADDICTION.get())) {
                if (level.isClientSide)
                    player.displayClientMessage(net.minecraft.network.chat.Component.translatable(
                            "message.legendarysurvivaloverhaul.morphine_use_under_painkiller_addiction"), true);
                return InteractionResultHolder.fail(stack);
            }

            if (!level.isClientSide)
                MedsAndHerbsUtil.triggerMorphineBehavior(player);

            return InteractionResultHolder.pass(stack);
        });
    }
}
