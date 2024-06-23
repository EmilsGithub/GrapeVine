package net.emilsg.grapevine.register;

import net.emilsg.grapevine.block.crop.CropModelType;
import net.emilsg.grapevine.block.crop.CropType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class CropFamily {
    private final String name;
    private final Item seedsItem;
    private final Item cropItem;
    private final Block cropBlock;
    private final int maxAge;
    private final boolean hasSpecialDrops;
    private final boolean hasSeeds;
    private final CropModelType cropModelType;
    private final CropType cropType;

    public CropFamily(String name, @Nullable Item seedsItem, Item cropItem, Block cropBlock, int maxAge, boolean hasSpecialDrops, boolean hasSeeds, CropModelType cropModelType, @Nullable CropType cropType) {
        this.name = name;
        this.seedsItem = seedsItem;
        this.cropItem = cropItem;
        this.cropBlock = cropBlock;
        this.maxAge = maxAge;
        this.hasSpecialDrops = hasSpecialDrops;
        this.hasSeeds = hasSeeds;
        this.cropModelType = cropModelType;
        this.cropType = cropType;
    }

    public String getName() {
        return name;
    }

    public Item getSeedsItem() {
        return seedsItem;
    }

    public Item getCropItem() {
        return cropItem;
    }

    public Block getCropBlock() {
        return cropBlock;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public boolean getHasSpecialDrops() {
        return hasSpecialDrops;
    }

    public boolean getHasSeeds() {
        return hasSeeds;
    }

    public CropModelType getCropModelType() {
        return cropModelType;
    }

    public CropType getCropType() {
        return cropType;
    }
}
