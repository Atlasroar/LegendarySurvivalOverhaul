package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.TickPhase;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class ModCapabilities
{
	public static void registerServerEvents()
	{
		ServerTickEvents.START_SERVER_TICK.register(server -> server.getPlayerList().getPlayers()
				.forEach(player -> onPlayerTick(player, TickPhase.START)));
		ServerTickEvents.END_SERVER_TICK.register(server -> server.getPlayerList().getPlayers()
				.forEach(player -> onPlayerTick(player, TickPhase.END)));
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			initializePlayer(handler.player);
			syncPlayerState(handler.player);
			FabricDataSyncHandler.syncAll(handler.player);
		});
		ServerPlayerEvents.COPY_FROM.register(ModCapabilities::copyPlayerState);
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, player, alive) -> {
			applyTemperatureImmunityOnDeathRespawn(player, alive);
			syncPlayerState(player);
		});
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> syncPlayerState(player));
		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.dimension() == Level.OVERWORLD)
				world.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION)
						.set(Config.Baked.naturalRegenerationEnabled, server);
		});
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
			if (success)
				server.getPlayerList().getPlayers().forEach(FabricDataSyncHandler::syncAll);
		});
	}

	private static void initializePlayer(net.minecraft.server.level.ServerPlayer player) {
		PlayerSurvivalComponent survival = PlayerSurvivalComponents.PLAYER_SURVIVAL.get(player);
		if (Config.Baked.temperatureImmunityOnFirstSpawnEnabled && Config.Baked.temperatureEnabled
				&& !survival.tempImmuneOnSpawn()) {
			survival.setTempImmuneOnSpawn(true);
			player.addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY.get(),
					Config.Baked.temperatureImmunityOnFirstSpawnTime, 0, false, false, true));
		}

		if (Config.Baked.healthOverhaulEnabled)
			HealthUtil.initializeHealthAttributes(player);

		HealthUtil.updatePlayerMaxHealthAttribute(player);
		BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
	}

	private static void applyTemperatureImmunityOnDeathRespawn(
			net.minecraft.server.level.ServerPlayer player, boolean oldPlayerAlive) {
		if (!oldPlayerAlive && Config.Baked.temperatureImmunityOnDeathEnabled
				&& Config.Baked.temperatureEnabled) {
			player.addEffect(new MobEffectInstance(MobEffectRegistry.TEMPERATURE_IMMUNITY.get(),
					Config.Baked.temperatureImmunityOnDeathTime, 0, false, false, true));
		}
	}

	public static void onPlayerTick(Player player, TickPhase phase)
	{
		if (player.level().isClientSide())
		{
			// Client Side
			if (shouldSkipTick(player)) return;

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);

				tempCap.tickClient(player, phase);
			}
		}
		else
		{
			// Server Side
			Level level = player.level();

			if (shouldSkipTick(player)) return;

			if (!Config.Baked.vanillaFreezeEnabled) {
				if (player.getTicksFrozen() > 0)
					player.setTicksFrozen(0);
			}

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);
				
				tempCap.tickUpdate(player, level, phase);
				
				if(phase == TickPhase.START && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					tempCap.setClean();
					sendTemperatureUpdate(player);
				}
			}
			
			if (Config.Baked.wetnessEnabled) {
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);
				
				wetCap.tickUpdate(player, level, phase);
				
				/**
				 * Because of the way wetness is ticked, if it's dirty, it's probably going to be dirty next tick,
				 * and if it's clean, it's probably going to be clean the next tick
				 * Thus, we don't want to clean up the wetness capability every single tick
				 * just because the player is standing out in the rain
				 * since it's not good for performance
				 */
				if (phase == TickPhase.START && (wetCap.getPacketTimer() % Config.Baked.routinePacketSync == 0 || wetCap.isDirty()))
				{
					wetCap.setClean();
					sendWetnessUpdate(player);
				}
			}

			if (Config.Baked.thirstEnabled) {
				ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

				thirstCap.tickUpdate(player, level, phase);

				if (phase == TickPhase.START && (thirstCap.isDirty() || thirstCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					thirstCap.setClean();
					sendThirstUpdate(player);
				}
			}

			if (Config.Baked.baseFoodExhaustion > 0) {
				FoodCapability foodCapability = CapabilityUtil.getFoodCapability(player);

				foodCapability.tickUpdate(player, level, phase);
			}

			if (Config.Baked.localizedBodyDamageEnabled) {
				BodyDamageCapability bodyDamageCapability = CapabilityUtil.getBodyDamageCapability(player);

				bodyDamageCapability.tickUpdate(player, level, phase);

				if(phase == TickPhase.START && (bodyDamageCapability.isDirty() || bodyDamageCapability.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					bodyDamageCapability.setClean();
					sendBodyDamageUpdate(player);
				}
			}

			if (Config.Baked.healthOverhaulEnabled) {
				HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

				if(phase == TickPhase.START && healthCapability.isDirty())
				{
					healthCapability.setClean();
					sendHealthUpdate(player);
				}
			}
		}
	}

	private static void copyPlayerState(net.minecraft.server.level.ServerPlayer oldPlayer,
			net.minecraft.server.level.ServerPlayer player, boolean alive)
	{
		PlayerSurvivalComponent oldState = PlayerSurvivalComponents.PLAYER_SURVIVAL.get(oldPlayer);
		PlayerSurvivalComponent newState = PlayerSurvivalComponents.PLAYER_SURVIVAL.get(player);

		if (alive) {
			net.minecraft.nbt.CompoundTag state = new net.minecraft.nbt.CompoundTag();
			oldState.writeToNbt(state);
			newState.readFromNbt(state);
		} else {
			newState.setTempImmuneOnSpawn(oldState.tempImmuneOnSpawn());
			newState.health().readNBT(oldState.health().writeNBT());
			newState.thirst().init();
			newState.bodyDamage().init();
		}

		if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.healthOverhaulEnabled)
			BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);

		if (Config.Baked.healthOverhaulEnabled) {
			HealthUtil.initializeHealthAttributes(player);
			if (!alive && Config.Baked.heartsLostOnDeath > 0)
				HealthUtil.loseHearth(player, Config.Baked.heartsLostOnDeath);
			else
				HealthUtil.updatePlayerMaxHealthAttribute(player);
			player.setHealth(player.getMaxHealth());
		}

		if (Config.Baked.localizedBodyDamageEnabled && !alive)
			BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);

	}

	private static void sendTemperatureUpdate(Player player)
	{
		syncPlayerState(player);
	}

	private static void sendWetnessUpdate(Player player)
	{
		syncPlayerState(player);
	}

	private static void sendThirstUpdate(Player player)
	{
		syncPlayerState(player);
	}

	private static void sendBodyDamageUpdate(Player player)
	{
		syncPlayerState(player);
	}

	private static void sendHealthUpdate(Player player)
	{
		syncPlayerState(player);
	}

	private static void syncPlayerState(Player player)
	{
		if (!player.level().isClientSide())
			PlayerSurvivalComponents.PLAYER_SURVIVAL.sync(player);
	}

	protected static boolean shouldSkipTick(Player player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
