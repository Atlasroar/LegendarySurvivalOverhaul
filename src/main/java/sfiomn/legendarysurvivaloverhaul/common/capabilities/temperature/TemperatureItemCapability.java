package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class TemperatureItemCapability implements ITemperatureItemCapability {
    private static final String DATA_KEY = "legendarysurvivaloverhaul_temperature";

    private float temperature;
    private long updateTick;
    private final ItemStack itemStack;

    public TemperatureItemCapability() {
        this(null);
    }

    public TemperatureItemCapability(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.init();
        if (itemStack != null) {
            CompoundTag tag = itemStack.getTagElement(DATA_KEY);
            if (tag != null) {
                this.readNBT(tag);
            }
        }
    }

    private void init() {
        this.temperature = TemperatureEnum.NORMAL.getValue();
        this.updateTick = 0;
    }

    @Override
    public boolean shouldUpdate(long currentTick) {
        return (currentTick - this.updateTick) > 10;
    }

    @Override
    public void updateWorldTemperature(Level world, Entity holder, long currentTick) {
        this.updateTick = currentTick;
        this.temperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
        this.save();
    }

    @Override
    public float getWorldTemperatureLevel() {
        return this.temperature;
    }

    @Override
    public void setWorldTemperatureLevel(float temperature) {
        this.temperature = temperature;
        this.save();
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();

        compound.putFloat("temperature", this.temperature);
        compound.putLong("update_tick", this.updateTick);

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();
        if (compound.contains("temperature"))
            this.temperature = compound.getFloat("temperature");
        if (compound.contains("update_tick"))
            this.updateTick = compound.getLong("update_tick");
    }

    private void save()
    {
        if (this.itemStack != null)
            this.itemStack.getOrCreateTagElement(DATA_KEY).merge(this.writeNBT());
    }
}
