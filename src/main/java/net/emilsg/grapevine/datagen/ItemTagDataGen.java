package net.emilsg.grapevine.datagen;

import net.emilsg.grapevine.block.crop.CropType;
import net.emilsg.grapevine.item.ModItemTags;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ItemTagDataGen extends FabricTagProvider.ItemTagProvider {

    public ItemTagDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
            if(cropFamily.getCropType() == null) return;
            CropType cropType = cropFamily.getCropType();
            Item cropItem = cropFamily.getCropItem();

            if(cropType.equals(CropType.BULBS)) {
                getOrCreateTagBuilder(ModItemTags.BULBS).add(cropItem);
            }

            if(cropType.equals(CropType.GRAINS)) {
                getOrCreateTagBuilder(ModItemTags.GRAINS).add(cropItem);
            }

            if(cropType.equals(CropType.FRUITS)) {
                getOrCreateTagBuilder(ModItemTags.FRUITS).add(cropItem);
            }

            if(cropType.equals(CropType.HERBS)) {
                getOrCreateTagBuilder(ModItemTags.HERBS).add(cropItem);
            }

            if(cropType.equals(CropType.LEAFY_GREENS)) {
                getOrCreateTagBuilder(ModItemTags.LEAFY_GREENS).add(cropItem);
            }

            if(cropType.equals(CropType.LEGUMES)) {
                getOrCreateTagBuilder(ModItemTags.LEGUMES).add(cropItem);
            }

            if(cropType.equals(CropType.TUBERS)) {
                getOrCreateTagBuilder(ModItemTags.TUBERS).add(cropItem);
            }
        });
    }
}
