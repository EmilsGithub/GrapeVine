package net.emilsg.grapevine.block.crop;

import net.minecraft.util.StringIdentifiable;

public enum CropType implements StringIdentifiable {
    FRUITS("fruits"),
    LEAFY_GREENS("leafy_greens"),
    HERBS("herbs"),
    GRAINS("grains"),
    LEGUMES("legumes"),
    TUBERS("tubers"),
    BULBS("bulbs");

    private final String name;

    CropType(String name) {
        this.name = name;
    }

    public String asString() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
