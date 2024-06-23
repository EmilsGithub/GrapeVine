package net.emilsg.grapevine.recipe.frying;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.emilsg.grapevine.recipe.AbstractCookingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class ShapelessFryingRecipe extends AbstractCookingRecipe implements FryingRecipe {
    private final Identifier id;
    final String group;
    final CraftingRecipeCategory category;
    final ItemStack output;
    final DefaultedList<Ingredient> input;

    public ShapelessFryingRecipe(Identifier id, String group, CraftingRecipeCategory category, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, category, output, input);
        this.id = id;
        this.group = group;
        this.category = category;
        this.output = output;
        this.input = input;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<ShapelessFryingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "frying";

        public ShapelessFryingRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            CraftingRecipeCategory craftingRecipeCategory = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
            DefaultedList<Ingredient> defaultedList = ShapelessFryingRecipe.Serializer.getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            }
            if (defaultedList.size() > 6) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            }
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new ShapelessFryingRecipe(identifier, string, craftingRecipeCategory, itemStack, defaultedList);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i), false);
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        @Override
        public ShapelessFryingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            CraftingRecipeCategory craftingRecipeCategory = packetByteBuf.readEnumConstant(CraftingRecipeCategory.class);
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);
            System.out.println(defaultedList);
            for (int j = 0; j < defaultedList.size(); ++j) {
                defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
            }
            ItemStack itemStack = packetByteBuf.readItemStack();
            System.out.println(itemStack);
            return new ShapelessFryingRecipe(identifier, string, craftingRecipeCategory, itemStack, defaultedList);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, ShapelessFryingRecipe shapelessFryingRecipe) {
            packetByteBuf.writeString(shapelessFryingRecipe.group);
            packetByteBuf.writeEnumConstant(shapelessFryingRecipe.category);
            packetByteBuf.writeVarInt(shapelessFryingRecipe.input.size());
            for (Ingredient ingredient : shapelessFryingRecipe.input) {
                ingredient.write(packetByteBuf);
            }
            packetByteBuf.writeItemStack(shapelessFryingRecipe.output);
        }
    }
}
