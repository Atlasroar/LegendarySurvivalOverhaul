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
import sfiomn.legendarysurvivaloverhaul.common.events.FabricEquipmentAttributeHooks;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.network.FabricDataSyncHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ModCapabilities
{
	/**
	 * Players whose current health should be forced back to their max health attribute value
	 * on their next server tick. Death respawns queue onto this instead of healing immediately
	 * in {@link #copyPlayerState}, because other mods (e.g. LevelZ/WandererZ) recompute max-health
	 * attribute modifiers from their own {@code AFTER_RESPAWN} listener, and Fabric does not
	 * guarantee that listener runs before or after ours. Waiting for the next tick lets every
	 * mod's respawn-time attribute math finish first, regardless of registration order, so the
	 * player is healed to their true combined max health instead of a stale LSO-only snapshot.
	 */
	private static final Set<UUID> PENDING_RESPAWN_FULL_HEAL = ConcurrentHashMap.newKeySet();

	/**
	 * Players whose death heart-loss ({@code heartsLostOnDeath}) should be applied on their
	 * next server tick instead of immediately in {@link #copyPlayerState}. The loseHearth floor
	 * check reads the player's current max-health attribute as an absolute reference point (how
	 * many hearts they currently have), which is only accurate once every other mod's respawn
	 * attribute math (e.g. LevelZ's/WandererZ's level-based max health) has also finished, for the same
	 * ordering reasons documented on {@link #PENDING_RESPAWN_FULL_HEAL}.
	 */
	private static final Set<UUID> PENDING_RESPAWN_HEART_LOSS = ConcurrentHashMap.newKeySet();

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
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			PENDING_RESPAWN_FULL_HEAL.remove(handler.player.getUUID());
			PENDING_RESPAWN_HEART_LOSS.remove(handler.player.getUUID());
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

			if (phase == TickPhase.START && PENDING_RESPAWN_HEART_LOSS.remove(player.getUUID()))
				HealthUtil.loseHearth(player, Config.Baked.heartsLostOnDeath);

			if (phase == TickPhase.START && PENDING_RESPAWN_FULL_HEAL.remove(player.getUUID()))
				player.setHealth(player.getMaxHealth());

			if (shouldSkipTick(player)) return;

			if (phase == TickPhase.END)
				FabricEquipmentAttributeHooks.updatePlayerEquipmentModifiers(player);

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

			if (alive) {
				// World/dimension change: no other mod re-derives max health here, safe to
				// update the attribute and heal now.
				HealthUtil.updatePlayerMaxHealthAttribute(player);
				player.setHealth(player.getMaxHealth());
			} else {
				// Death respawn: other mods (e.g. LevelZ/WandererZ) may still apply their own max-health
				// attribute changes from their AFTER_RESPAWN listener. Defer both the heart-loss
				// floor check (which reads the player's current max health as an absolute
				// reference point) and the full heal to this player's next tick, so they run
				// after every mod's respawn attribute math has finished, regardless of
				// registration order.
				if (Config.Baked.heartsLostOnDeath > 0)
					PENDING_RESPAWN_HEART_LOSS.add(player.getUUID());
				else
					HealthUtil.updatePlayerMaxHealthAttribute(player);
				PENDING_RESPAWN_FULL_HEAL.add(player.getUUID());
			}
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
