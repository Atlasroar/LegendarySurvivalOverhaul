package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationBlastingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.PurificationSmeltingRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.RemoveCoatRecipe;
import sfiomn.legendarysurvivaloverhaul.common.recipe.SewingRecipe;

public class RecipeRegistry {

    public static final FabricDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = FabricDeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER);

    public static final RegistryObject<RecipeSerializer<SewingRecipe>> SEWING_SERIALIZER = RECIPE_SERIALIZERS.register("sewing", () -> SewingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PurificationSmeltingRecipe>> PURIFICATION_SMELTING_SERIALIZER = RECIPE_SERIALIZERS.register("purification_smelting", () -> PurificationSmeltingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PurificationBlastingRecipe>> PURIFICATION_BLASTING_SERIALIZER = RECIPE_SERIALIZERS.register("purification_blasting", () -> PurificationBlastingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<RemoveCoatRecipe>> REMOVE_COAT_SERIALIZER = RECIPE_SERIALIZERS.register("remove_coat", () -> new SimpleCraftingRecipeSerializer<>(RemoveCoatRecipe::new));

    public static final FabricDeferredRegister<RecipeType<?>> RECIPE_TYPE = FabricDeferredRegister.create(BuiltInRegistries.RECIPE_TYPE);

    public static final RegistryObject<RecipeType<SewingRecipe>> SEWING_RECIPE = RECIPE_TYPE.register("sewing", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return SewingRecipe.Type.ID.toString();
        }
    });

    public static void register() {
        RECIPE_SERIALIZERS.registerAll();
        RECIPE_TYPE.registerAll();
    }
}
