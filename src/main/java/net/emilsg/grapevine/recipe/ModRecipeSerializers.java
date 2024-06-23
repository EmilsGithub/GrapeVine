package net.emilsg.grapevine.recipe;

import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.recipe.cooking.ShapelessCookingRecipe;
import net.emilsg.grapevine.recipe.frying.ShapelessFryingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializers {

    public static void registerRecipeSerializers() {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(GrapeVine.MOD_ID, ShapelessFryingRecipe.Serializer.ID), ShapelessFryingRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(GrapeVine.MOD_ID, FryingType.ID), FryingType.INSTANCE);

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(GrapeVine.MOD_ID, ShapelessCookingRecipe.Serializer.ID), ShapelessCookingRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(GrapeVine.MOD_ID, CookingType.ID), CookingType.INSTANCE);
    }

    public static class FryingType implements RecipeType<ShapelessFryingRecipe> {
        private FryingType() { }
        public static final FryingType INSTANCE = new FryingType();
        public static final String ID = "frying";
    }

    public static class CookingType implements RecipeType<ShapelessCookingRecipe> {
        private CookingType() { }
        public static final CookingType INSTANCE = new CookingType();
        public static final String ID = "cooking";
    }
}
