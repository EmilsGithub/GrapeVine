package net.emilsg.grapevine.recipe.cooking;

import net.emilsg.grapevine.recipe.ModRecipeSerializers;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CraftingRecipeCategory;

public interface CookingRecipe extends Recipe<RecipeInputInventory> {
    @Override
    default RecipeType<?> getType() {
        return ModRecipeSerializers.FryingType.INSTANCE;
    }

    public CraftingRecipeCategory getCategory();

    @Override
    default boolean isIgnoredInRecipeBook() {
        return true;
    }
}
