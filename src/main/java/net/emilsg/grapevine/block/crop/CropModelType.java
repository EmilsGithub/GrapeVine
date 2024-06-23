package net.emilsg.grapevine.block.crop;

import net.minecraft.util.StringIdentifiable;

public enum CropModelType implements StringIdentifiable {
    CROP ("crop"),
    CROSS ("cross"),
    TALL_CROP ("tall_crop"),
    TALL_CROSS ("tall_cross");

    private final String name;

    CropModelType(String name) {
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
