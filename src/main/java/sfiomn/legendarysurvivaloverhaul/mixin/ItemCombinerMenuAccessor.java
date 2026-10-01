package sfiomn.legendarysurvivaloverhaul.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuAccessor {
    @Accessor("inputSlots")
    Container legendarysurvivaloverhaul$getInputSlots();

    @Accessor("resultSlots")
    ResultContainer legendarysurvivaloverhaul$getResultSlots();
}
