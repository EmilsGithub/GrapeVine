package net.emilsg.grapevine.item.items;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class FoodItem extends Item {

    public FoodItem(Settings settings, int hunger, float saturationMultiplier) {
        super(settings.food(new FoodComponent.Builder().hunger(hunger).saturationModifier(saturationMultiplier).build()));
    }

}
