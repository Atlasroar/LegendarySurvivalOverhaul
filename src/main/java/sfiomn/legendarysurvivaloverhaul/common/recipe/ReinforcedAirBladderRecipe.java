package sfiomn.legendarysurvivaloverhaul.common.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.RecipeRegistry;

public class ReinforcedAirBladderRecipe extends CustomRecipe {
    public ReinforcedAirBladderRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(@NotNull CraftingContainer container, @NotNull Level level) {
        boolean hasBladder = false;
        boolean hasNetherite = false;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(ItemRegistry.AIR_BLADDER.get()) && !hasBladder) {
                hasBladder = true;
            } else if (stack.is(Items.NETHERITE_INGOT) && !hasNetherite) {
                hasNetherite = true;
            } else {
                return false;
            }
        }
        return hasBladder && hasNetherite;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.is(ItemRegistry.AIR_BLADDER.get())) {
                ItemStack result = new ItemStack(ItemRegistry.REINFORCED_AIR_BLADDER.get());
                if (stack.hasTag()) result.setTag(stack.getTag().copy());
                result.setDamageValue(Math.min(stack.getDamageValue(), result.getMaxDamage() - 1));
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingContainer container) {
        return NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.REINFORCED_AIR_BLADDER_SERIALIZER.get();
    }
}
