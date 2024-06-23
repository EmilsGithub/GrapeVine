package net.emilsg.grapevine.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor("recipeRemainder") @Mutable
    public void setRecipeRemainder(Item recipeRemainder);

}
