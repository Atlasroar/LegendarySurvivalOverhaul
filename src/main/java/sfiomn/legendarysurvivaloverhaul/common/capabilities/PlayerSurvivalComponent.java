package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;

public final class PlayerSurvivalComponent implements Component, AutoSyncedComponent
{
	private final TemperatureCapability temperature = new TemperatureCapability();
	private final WetnessCapability wetness = new WetnessCapability();
	private final ThirstCapability thirst = new ThirstCapability();
	private final HealthCapability health = new HealthCapability();
	private final FoodCapability food = new FoodCapability();
	private final BodyDamageCapability bodyDamage = new BodyDamageCapability();
	private boolean tempImmuneOnSpawn;

	public TemperatureCapability temperature()
	{
		return temperature;
	}

	public WetnessCapability wetness()
	{
		return wetness;
	}

	public ThirstCapability thirst()
	{
		return thirst;
	}

	public HealthCapability health()
	{
		return health;
	}

	public FoodCapability food()
	{
		return food;
	}

	public BodyDamageCapability bodyDamage()
	{
		return bodyDamage;
	}

	public boolean tempImmuneOnSpawn()
	{
		return tempImmuneOnSpawn;
	}

	public void setTempImmuneOnSpawn(boolean tempImmuneOnSpawn)
	{
		this.tempImmuneOnSpawn = tempImmuneOnSpawn;
	}

	@Override
	public void readFromNbt(CompoundTag tag)
	{
		temperature.readNBT(tag.getCompound("temperature"));
		wetness.readNBT(tag.getCompound("wetness"));
		thirst.readNBT(tag.getCompound("thirst"));
		health.readNBT(tag.getCompound("health"));
		bodyDamage.readNBT(tag.getCompound("body_damage"));
		tempImmuneOnSpawn = tag.getBoolean("temp_immune_on_spawn");
	}

	@Override
	public void writeToNbt(CompoundTag tag)
	{
		tag.put("temperature", temperature.writeNBT());
		tag.put("wetness", wetness.writeNBT());
		tag.put("thirst", thirst.writeNBT());
		tag.put("health", health.writeNBT());
		tag.put("body_damage", bodyDamage.writeNBT());
		tag.putBoolean("temp_immune_on_spawn", tempImmuneOnSpawn);
	}
}
