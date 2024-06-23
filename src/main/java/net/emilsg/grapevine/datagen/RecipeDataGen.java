package net.emilsg.grapevine.datagen;

import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RecipeDataGen extends FabricRecipeProvider {

    public RecipeDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
            boolean hasSeeds = cropFamily.getHasSeeds();

            Item cropItem = cropFamily.getCropItem();
            Item seedsItem = cropFamily.getSeedsItem();

            if(hasSeeds) offerSeedsRecipe(exporter, cropItem, seedsItem);

        });
    }

    private void offerSeedsRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible output) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, output)
                .input(input)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, new Identifier(GrapeVine.MOD_ID, getRecipeName(output)));
    }
}
