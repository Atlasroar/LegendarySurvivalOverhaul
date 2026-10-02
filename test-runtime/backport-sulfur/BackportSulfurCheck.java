import com.blackgear.vanillabackport.common.level.block_entities.PotentSulfurBlockEntity;
import com.mojang.authlib.GameProfile;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityLevel;
import sfiomn.legendarysurvivaloverhaul.api.airquality.AirQualityUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.UUID;

public final class BackportSulfurCheck implements ModInitializer {
    private static void check(boolean value, String description) {
        if (!value) throw new AssertionError(description);
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                var level = server.overworld();
                var player = new ServerPlayer(server, level, new GameProfile(UUID.randomUUID(), "SulfurCheck"));
                player.connection = new ServerGamePacketListenerImpl(
                        server, new Connection(PacketFlow.SERVERBOUND), player);
                player.setPos(8.5, 66, 8.5);
                var eye = BlockPos.containing(player.getEyePosition());
                level.getChunkAt(eye);
                var source = server.createCommandSourceStack().withSuppressedOutput();
                check(server.getCommands().performPrefixedCommand(source,
                        "fillbiome 0 64 0 15 80 15 minecraft:sulfur_caves") > 0, "Set sulfur biome");
                check(AirQualityUtil.computeAirQualityAtLocation(level, player.getEyePosition())
                        == AirQualityLevel.YELLOW, "Sulfur cave ambient YELLOW");
                server.getWorldData().overworldData().setGameTime(2);
                check(AirQualityLevel.YELLOW.getAirAmountAfterProtection(player) == -1, "Nether drain interval");
                server.getWorldData().overworldData().setGameTime(3);
                check(AirQualityLevel.YELLOW.getAirAmountAfterProtection(player) == 0, "No drain between attempts");

                var provider = eye.offset(2, 0, 0);
                level.setBlockAndUpdate(provider, Blocks.SOUL_TORCH.defaultBlockState());
                check(AirQualityUtil.computeAirQualityAtLocation(level, player.getEyePosition())
                        == AirQualityLevel.BLUE, "Soul provider overrides cave ambient");
                level.setBlockAndUpdate(provider, Blocks.LAVA.defaultBlockState());
                check(AirQualityUtil.computeAirQualityAtLocation(level, player.getEyePosition())
                        == AirQualityLevel.RED, "Lava provider remains RED");
                level.setBlockAndUpdate(provider, Blocks.AIR.defaultBlockState());

                Config.AIR.sulfurCaveAirEnabled.validateAndSet(false);
                Config.bake(Config.AIR);
                check(AirQualityUtil.computeAirQualityAtLocation(level, player.getEyePosition())
                        == AirQualityLevel.GREEN, "Sulfur toggle restores dimension profile");
                Config.AIR.sulfurCaveAirEnabled.validateAndSet(true);
                Config.bake(Config.AIR);

                PotentSulfurBlockEntity.expose(player);
                check(player.hasEffect(MobEffects.CONFUSION), "Unprotected sulfur nausea retained");
                player.removeEffect(MobEffects.CONFUSION);
                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.RESPIRATOR.get()));
                PotentSulfurBlockEntity.expose(player);
                check(player.hasEffect(MobEffects.CONFUSION), "Held mask does not protect");
                player.removeEffect(MobEffects.CONFUSION);
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.TURTLE_HELMET));
                PotentSulfurBlockEntity.expose(player);
                check(player.hasEffect(MobEffects.CONFUSION), "Turtle helmet is not a sulfur mask");
                player.removeEffect(MobEffects.CONFUSION);
                player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.RESPIRATOR.get()));
                PotentSulfurBlockEntity.expose(player);
                check(!player.hasEffect(MobEffects.CONFUSION), "Worn head mask prevents sulfur nausea");
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80));
                check(!player.hasEffect(MobEffects.CONFUSION), "Direct nausea from any source is blocked");
                check(!player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80), player),
                        "Source-bearing nausea overload is blocked");
                check(player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80)),
                        "Unrelated effect remains allowed");
                player.removeEffect(MobEffects.CONFUSION);
                player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);

                var component = TrinketsApi.getTrinketComponent(player).orElseThrow();
                component.update();
                var face = component.getInventory().get("head").get("face");
                face.setItem(0, new ItemStack(ItemRegistry.RESPIRATOR.get()));
                PotentSulfurBlockEntity.expose(player);
                check(!player.hasEffect(MobEffects.CONFUSION), "Equipped Trinkets mask prevents sulfur nausea");
                Config.AIR.airQualityEnabled.validateAndSet(false);
                Config.bake(Config.AIR);
                check(AirQualityUtil.computeAirQualityAtLocation(level, player.getEyePosition())
                        == AirQualityLevel.GREEN, "Global air toggle respected");
                PotentSulfurBlockEntity.expose(player);
                check(!player.hasEffect(MobEffects.CONFUSION), "Nausea protection independent of air toggle");
                Config.AIR.respiratorBlocksSulfurNausea.validateAndSet(false);
                Config.bake(Config.AIR);
                PotentSulfurBlockEntity.expose(player);
                check(player.hasEffect(MobEffects.CONFUSION), "Nausea protection toggle respected");
                System.out.println("BACKPORT_SULFUR_CONTRACT_CHECK_PASSED");
            } finally {
                server.halt(false);
            }
        });
    }
}
