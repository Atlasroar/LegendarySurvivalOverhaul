import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.TickPhase;
import sfiomn.legendarysurvivaloverhaul.common.blocks.NetherLilyBlock;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.items.armor.IceSkating;
import sfiomn.legendarysurvivaloverhaul.common.temperature.WetModifier;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.BlockRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.UUID;

public final class WetContentCheck implements ModInitializer {
    static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }

    static void close(double actual, double expected, String message) {
        check(Math.abs(actual - expected) < 0.0001, message + ": " + actual + " != " + expected);
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            try {
                for (String id : new String[] {"frostiful", "scorchful", "thermoo"})
                    check(!FabricLoader.getInstance().isModLoaded(id), "No required upstream mod: " + id);
                ServerLevel level = server.overworld();
                level.getChunk(0, 0);
                ServerPlayer player = new ServerPlayer(server, level, new GameProfile(UUID.randomUUID(), "WetCheck"));
                player.connection = new ServerGamePacketListenerImpl(server, new Connection(PacketFlow.SERVERBOUND), player);
                player.setPos(8.5, 200, 8.5);
                Config.Baked.temperatureEnabled = true;
                Config.Baked.wetnessEnabled = true;
                Config.Baked.immersiveWeatheringStormsEnabled = false;
                Config.Baked.tempTickTime = 5;
                Config.Baked.minTemperatureModification = Config.Baked.maxTemperatureModification = 0.5;
                Config.Baked.wetCoolingRateMultiplier = 1.5;
                Config.Baked.wetMultiplier = -10;
                Config.Baked.dangerousColdTemperature = Config.Baked.dangerousHeatTemperature = false;
                TemperatureCapability temp = CapabilityUtil.getTempCapability(player);
                WetnessCapability wet = CapabilityUtil.getWetnessCapability(player);
                for (var key : java.util.List.of(Biomes.PLAINS, Biomes.DESERT, Biomes.JUNGLE,
                        Biomes.SNOWY_PLAINS, Biomes.NETHER_WASTES, Biomes.THE_END)) {
                    var biome = level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(key);
                    level.getChunk(0, 0).fillBiomesFromNoise((x, y, z, sampler) -> biome,
                            level.getChunkSource().randomState().sampler());
                    wet.setWetness(0);
                    float target = TemperatureUtil.getPlayerTargetTemperature(player);
                    temp.setTemperatureLevel(target + 20);
                    tick(temp, player);
                    close(temp.getTemperatureLevel(), target + 19.5, "Dry baseline in " + key.location());
                    wet.setWetness(1);
                    temp.setTemperatureLevel(target + 20);
                    tick(temp, player);
                    close(temp.getTemperatureLevel(), target + 19.25, "1.5x cooling at any wetness in " + key.location());
                    wet.setWetness(400);
                    close(new WetModifier().getPlayerInfluence(player), -10, "Full wetness keeps original target offset");
                    float wetTarget = TemperatureUtil.getPlayerTargetTemperature(player);
                    temp.setTemperatureLevel(wetTarget - 10);
                    tick(temp, player);
                    close(temp.getTemperatureLevel(), wetTarget - 9.5, "Wet warming is not multiplied");
                    temp.setTemperatureLevel(wetTarget + 0.1F);
                    tick(temp, player);
                    close(temp.getTemperatureLevel(), wetTarget, "Wet cooling cannot overshoot");
                }
                Config.Baked.wetCoolingRateMultiplier = 2;
                temp.setTemperatureLevel(TemperatureUtil.getPlayerTargetTemperature(player) + 10);
                float before = temp.getTemperatureLevel();
                tick(temp, player);
                close(temp.getTemperatureLevel(), before - 1, "Custom wet cooling multiplier");
                Config.Baked.wetnessEnabled = false;
                temp.setTemperatureLevel(TemperatureUtil.getPlayerTargetTemperature(player) + 10);
                before = temp.getTemperatureLevel();
                tick(temp, player);
                close(temp.getTemperatureLevel(), before - 0.5, "Disabled wetness ignores saved saturation");
                Config.Baked.wetnessEnabled = true;
                wet.setWetness(200);
                close(new WetModifier().getPlayerInfluence(player), -5, "Half saturation target offset");
                lilies(level, player);
                skates(level);
                for (var item : new net.minecraft.world.item.Item[] {ItemRegistry.SNOW_HELMET.get(),
                        ItemRegistry.SNOW_CHEST.get(), ItemRegistry.SNOW_LEGGINGS.get(), ItemRegistry.SNOW_BOOTS.get()}) {
                    ArmorItem armor = (ArmorItem) item;
                    int expected = armor.getType() == ArmorItem.Type.CHESTPLATE ? 2 : 1;
                    check(armor.getDefense() == expected, "Fur replacement keeps original armor values");
                }
                check(server.getRecipeManager().byKey(id("ice_skates")).isPresent(), "Skates obtainable by recipe");
                var skateTemperature = TemperatureDataManager.getItem(id("ice_skates"));
                check(skateTemperature != null, "Skate protection data loaded");
                close(skateTemperature.coldResistance, 0.5, "Skates retain Fur Boots cold resistance");
                System.out.println("WET_CONTENT_CHECK_PASSED");
            } finally {
                server.halt(false);
            }
        });
    }

    static void tick(TemperatureCapability temp, ServerPlayer player) {
        temp.setTemperatureTickTimer(4);
        temp.tickUpdate(player, player.level(), TickPhase.END);
    }

    static void lilies(ServerLevel level, ServerPlayer player) {
        BlockPos pos = new BlockPos(8, 200, 8);
        level.setBlockAndUpdate(pos.below(), Blocks.CRIMSON_NYLIUM.defaultBlockState());
        var crimson = BlockRegistry.CRIMSON_LILY.get().defaultBlockState().setValue(NetherLilyBlock.WATER_LEVEL, 3);
        level.setBlockAndUpdate(pos, crimson);
        crimson.entityInside(level, pos, player);
        check(CapabilityUtil.getWetnessCapability(player).getWetness() == 400, "Crimson burst fully soaks LSO player");
        check(level.getBlockState(pos).getValue(NetherLilyBlock.WATER_LEVEL) == 0, "Burst drains crimson lily");
        ItemStack potion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING);
        player.setItemInHand(InteractionHand.MAIN_HAND, potion);
        check(use(level, pos, player) == InteractionResult.PASS, "Non-water potion rejected");
        player.setItemInHand(InteractionHand.MAIN_HAND, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
        check(use(level, pos, player).consumesAction(), "Water bottle fills crimson lily");
        check(player.getMainHandItem().is(Items.GLASS_BOTTLE), "Bottle returned after filling");
        check(level.getBlockState(pos).getValue(NetherLilyBlock.WATER_LEVEL) == 3, "Bottle fully fills lily");
        var warped = BlockRegistry.WARPED_LILY.get().defaultBlockState().setValue(NetherLilyBlock.WATER_LEVEL, 3);
        level.setBlockAndUpdate(pos, warped);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GLASS_BOTTLE));
        use(level, pos, player);
        check(PotionUtils.getPotion(player.getMainHandItem()) == Potions.WATER, "Warped harvest yields water bottle");
        check(level.getBlockState(pos).getValue(NetherLilyBlock.WATER_LEVEL) == 0, "Harvest drains warped lily");
        level.setBlockAndUpdate(pos, warped);
        ItemStack canteen = new ItemStack(ItemRegistry.CANTEEN.get());
        player.setItemInHand(InteractionHand.MAIN_HAND, canteen);
        use(level, pos, player);
        check(ThirstUtil.getCapacityTag(canteen) == 1, "Warped lily fills LSO canteen");
        level.setBlockAndUpdate(pos, crimson.setValue(NetherLilyBlock.WATER_LEVEL, 0));
        use(level, pos, player);
        check(ThirstUtil.getCapacityTag(canteen) == 0, "Crimson lily consumes one canteen serving");
        check(level.getBlockState(pos).getValue(NetherLilyBlock.WATER_LEVEL) == 3, "Canteen fills crimson lily");
        check(Block.getDrops(crimson, level, pos, null).stream().anyMatch(s -> s.is(BlockRegistry.CRIMSON_LILY.get().asItem())),
                "Lily has survival harvest loot");
        level.setBlockAndUpdate(pos.below(), Blocks.AIR.defaultBlockState());
        check(level.getBlockState(pos).isAir(), "Unsupported lily breaks");
        var nether = level.getServer().getLevel(net.minecraft.world.level.Level.NETHER);
        check(nether != null, "Nether available");
        nether.getChunk(0, 0);
        nether.setBlockAndUpdate(pos.below(), Blocks.NETHERRACK.defaultBlockState());
        var empty = crimson.setValue(NetherLilyBlock.WATER_LEVEL, 0);
        nether.setBlockAndUpdate(pos, empty);
        for (int i = 0; i < 200; i++) empty.randomTick(nether, pos, RandomSource.create(i));
        check(nether.getBlockState(pos).getValue(NetherLilyBlock.WATER_LEVEL) == 0, "Dry Nether substrate does not refill");
        nether.setBlockAndUpdate(pos.below(), Blocks.CRIMSON_NYLIUM.defaultBlockState());
        for (int i = 0; i < 200; i++)
            nether.getBlockState(pos).randomTick(nether, pos, RandomSource.create(i));
        check(nether.getBlockState(pos).getValue(NetherLilyBlock.WATER_LEVEL) == 3, "Nylium refills in Nether");
        for (String name : new String[] {"crimson_lily_patch", "warped_lily_patch"}) {
            var feature = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(id(name));
            check(feature != null, "Configured feature registered: " + name);
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++)
                    level.setBlockAndUpdate(new BlockPos(x, 199, z),
                            name.startsWith("crimson") ? Blocks.CRIMSON_NYLIUM.defaultBlockState() : Blocks.WARPED_NYLIUM.defaultBlockState());
            check(feature.place(level, level.getChunkSource().getGenerator(), RandomSource.create(42), pos),
                    "Actual feature can generate lilies: " + name);
        }
    }

    static InteractionResult use(ServerLevel level, BlockPos pos, ServerPlayer player) {
        return level.getBlockState(pos).use(level, player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
    }

    static void skates(ServerLevel level) {
        var zombie = EntityType.ZOMBIE.create(level);
        check(zombie != null, "Test skater created");
        zombie.setNoAi(false);
        zombie.setNoGravity(true);
        zombie.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.ICE_SKATES.get()));
        for (int x = 5; x <= 11; x++)
            for (int z = 5; z <= 11; z++)
                level.setBlockAndUpdate(new BlockPos(x, 199, z), Blocks.ICE.defaultBlockState());
        zombie.setPos(8.5, 200, 8.5);
        zombie.setOnGround(true);
        zombie.setDeltaMovement(0.3, 0, 0);
        zombie.travel(Vec3.ZERO);
        close(zombie.getDeltaMovement().x, 0.3 * 1.075 * 0.91, "Actual travel uses skate friction mixin");
        zombie.setNoAi(true);
        close(IceSkating.friction(zombie, Blocks.STONE.defaultBlockState(), 0.6F), 0.6, "No ice effect on stone");
        zombie.setShiftKeyDown(true);
        close(IceSkating.friction(zombie, Blocks.ICE.defaultBlockState(), 0.98F), 1, "Sneak brakes");
        zombie.setShiftKeyDown(false);
        zombie.setSprinting(true);
        close(IceSkating.friction(zombie, Blocks.BLUE_ICE.defaultBlockState(), 0.989F), 1.078, "Sprint accelerates on blue ice");
        zombie.setSprinting(false);
        level.setBlockAndUpdate(new BlockPos(8, 199, 8), Blocks.STONE.defaultBlockState());
        zombie.setPos(8.5, 200, 8.5);
        zombie.setDeltaMovement(Vec3.ZERO);
        zombie.setOnGround(true);
        double base = zombie.getAttributeValue(Attributes.MOVEMENT_SPEED);
        zombie.aiStep();
        close(zombie.getAttributeValue(Attributes.MOVEMENT_SPEED), base * 0.5, "Actual movement tick applies off-ice penalty");
        zombie.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        zombie.aiStep();
        close(zombie.getAttributeValue(Attributes.MOVEMENT_SPEED), base, "Removing skates cleans walking penalty");
        zombie.discard();
    }

    static ResourceLocation id(String path) {
        return new ResourceLocation("legendarysurvivaloverhaul", path);
    }
}
