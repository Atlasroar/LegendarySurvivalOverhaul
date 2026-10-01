package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.List;

public class NetherChaliceItem extends WearableTrinketItem {
    public NetherChaliceItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        tooltipComponents.add(Component.translatable("tooltip." + LegendarySurvivalOverhaul.MOD_ID + ".nether_chalice.description"));
    }

}
