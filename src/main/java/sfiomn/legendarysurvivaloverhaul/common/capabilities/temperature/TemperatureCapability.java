package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.common.TickPhase;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.effects.FrostbiteEffect;
import sfiomn.legendarysurvivaloverhaul.common.effects.HeatStrokeEffect;
import sfiomn.legendarysurvivaloverhaul.common.integration.weathering.WeatheringStorms;
import sfiomn.legendarysurvivaloverhaul.common.integration.weathering.WeatheringStorms.Exposure;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Code adapted from 
// https://github.com/Charles445/SimpleDifficulty/blob/v0.3.4/src/main/java/com/charles445/simpledifficulty/capability/TemperatureCapability.java

public class TemperatureCapability implements ITemperatureCapability
{
	private float temperature;
	private Set<Integer> temperatureImmunities;
	private int temperatureTickTimer;
	private int freezeTickTimer;
	private int freezingWindHungerTicks;
	private boolean stormColdHunger;
	private boolean stormHeatThirst;
	
	//Unsaved data
	private float oldTemperature;
	private float targetTemp;
	private boolean manualDirty;
	private int packetTimer;
	
	public TemperatureCapability() 
	{
		this.init();
	}
	
	public void init()
	{
		this.temperature = TemperatureEnum.NORMAL.getValue();
		this.temperatureImmunities = new HashSet<>();
		this.temperatureTickTimer = 0;
		this.freezeTickTimer = 0;
		
		this.oldTemperature = 0;
		this.targetTemp = 0;
		this.manualDirty = false;
		this.packetTimer = 0;
		this.freezingWindHungerTicks = 0;
		this.stormColdHunger = false;
		this.stormHeatThirst = false;
	}
	
	@Override
	public float getTemperatureLevel()
	{
		return temperature;
	}

	@Override
	public float getTargetTemperatureLevel()
	{
		return targetTemp;
	}

	@Override
	public int getTemperatureTickTimer()
	{
		return temperatureTickTimer;
	}

	@Override
	public int getFreezeTickTimer()
	{
		return freezeTickTimer;
	}

	@Override
	public void setTemperatureLevel(float temperature)
	{
		this.temperature = temperature;
	}

	@Override
	public void setTargetTemperatureLevel(float targetTemperature) {
		this.targetTemp = targetTemperature;
	}

	@Override
	public void setTemperatureTickTimer(int tickTimer)
	{
		this.temperatureTickTimer = tickTimer;
	}

	@Override
	public void setFreezeTickTimer(int tickTimer)
	{
		this.freezeTickTimer = tickTimer;
	}

	@Override
	public void addTemperatureLevel(float temperature)
	{
		this.setTemperatureLevel(getTemperatureLevel() + temperature);
	}

	@Override
	public void addTemperatureTickTimer(int tickTimer)
	{
		this.setTemperatureTickTimer(this.getTemperatureTickTimer() + tickTimer);
	}

	@Override
	public void addFreezeTickTimer(int tickTimer)
	{
		this.setFreezeTickTimer(Mth.clamp(this.getFreezeTickTimer() + tickTimer, 0, Config.Baked.maxFreezeEffectTick));
	}

	@Override
	public void addTemperatureImmunityId(int immunityId) {
		this.temperatureImmunities.add(immunityId);
	}

	@Override
	public void removeTemperatureImmunityId(int immunityId) {
		this.temperatureImmunities.remove(immunityId);
	}

	@Override
	public void tickUpdate(Player player, Level level, TickPhase phase)
	{
		if(phase == TickPhase.START)
		{
			packetTimer++;
			return;
		}

		addTemperatureTickTimer(1);
		if (freezingWindHungerTicks > 0)
			freezingWindHungerTicks--;
		Exposure storm = WeatheringStorms.exposure(player);

		if (player.isFreezing())
			addFreezeTickTimer(1);
		else if (getFreezeTickTimer() > 0)
			addFreezeTickTimer(-1);
		
		if (getTemperatureTickTimer() >= Config.Baked.tempTickTime) {
			setTemperatureTickTimer(0);

			this.targetTemp = TemperatureUtil.getPlayerTargetTemperature(player);
			if (storm == Exposure.SNOW)
				this.targetTemp = Math.min(this.targetTemp, TemperatureEnum.FROSTBITE.getValue());
			else if (storm == Exposure.SAND)
				this.targetTemp = Math.max(this.targetTemp, TemperatureEnum.HEAT_STROKE.getValue());

			if (getTemperatureLevel() != this.targetTemp) {
				boolean accelerated = (storm == Exposure.SNOW && getTemperatureLevel() > this.targetTemp)
						|| (storm == Exposure.SAND && getTemperatureLevel() < this.targetTemp);
				tickTemperature(getTemperatureLevel(), this.targetTemp, accelerated ? 2.0 : 1.0);
			}

			TemperatureEnum tempEnum = getTemperatureEnum();

			if (player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.DEBUG_STICK) {
				LegendarySurvivalOverhaul.LOGGER.info(tempEnum + ", " + getTemperatureLevel() + " -> " + this.targetTemp);
			}

			applyDangerousEffects(player, tempEnum);
		}
		applySecondaryEffects(player, getTemperatureEnum(), storm);
	}

	@Override
	public void tickClient(Player player, TickPhase phase) {
		if(phase == TickPhase.START) {
			return;
		}

		if (getTemperatureEnum() == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player))
			shakePlayer(player);
	}

	private void applyDangerousEffects(Player player, TemperatureEnum tempEnum) {
		if (Config.Baked.dangerousHeatTemperature && ThirstUtil.isThirstActive(player) && tempEnum == TemperatureEnum.HEAT_STROKE) {
			if (TemperatureEnum.HEAT_STROKE.getValue() <= getTemperatureLevel() && !HeatStrokeEffect.playerIsImmuneToHeat(player)) {
				// Apply hyperthermia
				if (!player.hasEffect(MobEffectRegistry.HEAT_STROKE.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.HEAT_STROKE.get(), -1, 0, false, true));
				return;
			}
		} else if (Config.Baked.dangerousColdTemperature && tempEnum == TemperatureEnum.FROSTBITE) {
			if (TemperatureEnum.FROSTBITE.getValue() >= getTemperatureLevel() && !FrostbiteEffect.playerIsImmuneToFrost(player)) {
				// Apply hypothermia.json
				if (!player.hasEffect(MobEffectRegistry.FROSTBITE.get()))
					player.addEffect(new MobEffectInstance(MobEffectRegistry.FROSTBITE.get(), -1, 0, false, true));
				return;
			}
		}
		if (player.hasEffect(MobEffectRegistry.HEAT_STROKE.get()))
			player.removeEffect(MobEffectRegistry.HEAT_STROKE.get());
		if (player.hasEffect(MobEffectRegistry.FROSTBITE.get()))
			player.removeEffect(MobEffectRegistry.FROSTBITE.get());
	}

	private void applySecondaryEffects(Player player, TemperatureEnum tempEnum, Exposure storm) {
		boolean heat = Config.Baked.heatTemperatureSecondaryEffects
				&& tempEnum == TemperatureEnum.HEAT_STROKE && !HeatStrokeEffect.playerIsImmuneToHeat(player);
		boolean cold = Config.Baked.coldTemperatureSecondaryEffects
				&& tempEnum == TemperatureEnum.FROSTBITE && !FrostbiteEffect.playerIsImmuneToFrost(player);
		stormHeatThirst = storm == Exposure.SAND && !heat;
		stormColdHunger = storm == Exposure.SNOW && !cold;
		reconcileSecondaryEffect(player, MobEffectRegistry.HEAT_THIRST.get(), heat || stormHeatThirst, 0);
		reconcileSecondaryEffect(player, MobEffectRegistry.COLD_HUNGER.get(), cold || stormColdHunger, freezingWindHungerTicks);
	}

	public void clearStormEffects(Player player) {
		if (stormHeatThirst)
			reconcileSecondaryEffect(player, MobEffectRegistry.HEAT_THIRST.get(), false, 0);
		if (stormColdHunger)
			reconcileSecondaryEffect(player, MobEffectRegistry.COLD_HUNGER.get(), false, freezingWindHungerTicks);
		stormHeatThirst = false;
		stormColdHunger = false;
		freezingWindHungerTicks = 0;
	}

	private void reconcileSecondaryEffect(Player player, MobEffect effect, boolean continuous, int remainingTicks) {
		MobEffectInstance current = player.getEffect(effect);
		if (continuous) {
			if (current == null || !current.isInfiniteDuration())
				player.addEffect(new MobEffectInstance(effect, -1, 0, false, false));
		} else if (current != null && current.isInfiniteDuration()) {
			player.removeEffect(effect);
			if (remainingTicks > 0)
				player.addEffect(new MobEffectInstance(effect, remainingTicks, 0, false, false));
		}
	}

	public void onFreezingWindHit(Player player) {
		freezingWindHungerTicks = 200;
		MobEffectInstance current = player.getEffect(MobEffectRegistry.COLD_HUNGER.get());
		if (current == null || !current.isInfiniteDuration())
			player.addEffect(new MobEffectInstance(MobEffectRegistry.COLD_HUNGER.get(), 200, 0, false, false));
	}

	private void shakePlayer(Player player) {
		// PI * 0.4 = 1.25663706144
		player.setYBodyRot(player.getYRot() + (float) (Math.cos((double) player.tickCount * 3.25D) * 1.25663706144));
	}
	
	private void tickTemperature(float currentTemp, float destination, double rate)
	{
		float diff = Math.abs(destination - currentTemp);
		
		double temperatureTowards = ((diff * (Config.Baked.maxTemperatureModification - Config.Baked.minTemperatureModification)) / (TemperatureEnum.getMax() - TemperatureEnum.getMin())) + Config.Baked.minTemperatureModification;

		temperatureTowards = Math.min(temperatureTowards * rate, diff);
		
		if (currentTemp > destination)
		{
			addTemperatureLevel((float) -temperatureTowards);
		}
		else 
		{
			addTemperatureLevel((float) temperatureTowards);
		}
	}

	@Override
	public boolean isDirty()
	{
		return manualDirty || this.temperature != this.oldTemperature;
	}

	@Override
	public void setClean()
	{
		this.oldTemperature = this.temperature;
		this.manualDirty = false;
	}

	@Override
	public int getPacketTimer()
	{
		return packetTimer;
	}

	@Override
	public TemperatureEnum getTemperatureEnum()
	{
		return TemperatureEnum.get(temperature);
	}

	@Override
	public List<Integer> getTemperatureImmunities() {
		return new ArrayList<>(this.temperatureImmunities);
	}

	public CompoundTag writeNBT() 
	{
		CompoundTag compound = new CompoundTag();
		
		compound.putFloat("temperature", this.getTemperatureLevel());
		compound.putFloat("targettemperature", this.getTargetTemperatureLevel());
		compound.putInt("ticktimer", this.getTemperatureTickTimer());
		compound.putInt("freezeticktimer", this.getFreezeTickTimer());
		compound.putIntArray("immunities", this.getTemperatureImmunities());
		compound.putInt("freezingwindhunger", freezingWindHungerTicks);
		compound.putBoolean("stormcoldhunger", stormColdHunger);
		compound.putBoolean("stormheatthirst", stormHeatThirst);
		
		return compound;
	}
	
	public void readNBT(CompoundTag compound)
	{
		this.init();
		if (compound.contains("temperature"))
			this.setTemperatureLevel(compound.getFloat("temperature"));
		if (compound.contains("targettemperature"))
			this.setTargetTemperatureLevel(compound.getFloat("targettemperature"));
		if (compound.contains("tickTimer"))
			this.setTemperatureTickTimer(compound.getInt("tickTimer"));
		if (compound.contains("freezeticktimer"))
			this.setFreezeTickTimer(compound.getInt("freezeticktimer"));
		if (compound.contains("immunities"))
			for (int immunityId: compound.getIntArray("immunities"))
				this.addTemperatureImmunityId(immunityId);
		freezingWindHungerTicks = Mth.clamp(compound.getInt("freezingwindhunger"), 0, 200);
		stormColdHunger = compound.getBoolean("stormcoldhunger");
		stormHeatThirst = compound.getBoolean("stormheatthirst");
	}
}
