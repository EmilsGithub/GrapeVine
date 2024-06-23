package net.emilsg.grapevine.register;

import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.block.crafting.CookingPotBlock;
import net.emilsg.grapevine.block.crafting.FryingPanBlock;
import net.emilsg.grapevine.block.crop.*;
import net.emilsg.grapevine.item.items.SeedsItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModRegistries {
    private static final FabricItemSettings RAW_EDIBLE_CROP_DEFAULT = new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1f).build());
    private static final FabricBlockSettings BASE_CROP_GREEN = FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).pistonBehavior(PistonBehavior.DESTROY);
    private static final FabricBlockSettings BASE_CROP_YELLOW = BASE_CROP_GREEN.mapColor(MapColor.PALE_YELLOW);
    private static final FabricBlockSettings BASE_CROP_BROWN = BASE_CROP_GREEN.mapColor(MapColor.BROWN);


    private static final Map<String, CropFamily> cropFamilies = new HashMap<>();

    public static void initRegistriesAndCrops() {
        registerCropFamily("grapes", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.FRUITS);
        registerCropFamily("lettuce", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEAFY_GREENS);
        registerCropFamily("cabbage", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEAFY_GREENS);
        registerCropFamily("corn", CropEnumType.TALL_CROSS_3, BASE_CROP_GREEN, false, true, CropType.GRAINS);
        registerCropFamily("garlic", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.BULBS);
        registerCropFamily("onion", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.BULBS);
        registerCropFamily("rice", CropEnumType.WATERLOGGED_TALL_CROSS_4, BASE_CROP_GREEN, false, true, CropType.GRAINS);
        registerCropFamily("chili", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("bell_pepper", CropEnumType.CROP_4, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("tomato", CropEnumType.TALL_CROP_4, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("eggplant", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("long_beans", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCropFamily("peanut", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCropFamily("basil", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.HERBS);
        registerCropFamily("mint", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.HERBS);
        registerCropFamily("peas", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCropFamily("bean_sprout", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("spring_onion", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.BULBS);
        registerCropFamily("sweet_potato", CropEnumType.CROP_3, BASE_CROP_GREEN, false, false, CropType.TUBERS);
        registerCropFamily("spinach", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEAFY_GREENS);
        registerCropFamily("mustard", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("soy_bean", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCropFamily("ginger", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, null);
        registerCropFamily("pepper", CropEnumType.CROP_3, BASE_CROP_GREEN, false, true, null);
    }

    public static final Item KNIFE = registerItem("knife", new Item(new FabricItemSettings()));
    public static final Item BUTCHER_KNIFE = registerItem("butcher_knife", new Item(new FabricItemSettings()));

    public static final Block FRYING_PAN = registerBlock("frying_pan", new FryingPanBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).breakInstantly()));
    public static final Block COOKING_POT = registerBlock("cooking_pot", new CookingPotBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).breakInstantly()));

     /**
     Helper Methods
     **/

     public static void registerCropFamily(String name, CropEnumType cropEnumType, FabricBlockSettings blockSettings, boolean hasSpecialDrops, boolean hasSeeds, @Nullable CropType cropType) {
         AgeCropBlock ageCropBlock;
         CropModelType cropModelType = cropEnumType.getCropModelType();
         int maxAge = cropEnumType.getTypeNumber();
         Item seedsItem = null;
         Item cropItem;

         ageCropBlock = switch (cropEnumType) {
             case CROP_3, CROSS_3 -> new AgeCropBlock3(blockSettings);
             case CROP_4, CROSS_4 -> new AgeCropBlock4(blockSettings);
             case CROP_5, CROSS_5 -> new AgeCropBlock5(blockSettings);

             case TALL_CROP_3, TALL_CROSS_3 -> new DoubleTallAgeCropBlock3(blockSettings);
             case TALL_CROP_4, TALL_CROSS_4 -> new DoubleTallAgeCropBlock4(blockSettings);
             case TALL_CROP_5, TALL_CROSS_5 -> new DoubleTallAgeCropBlock5(blockSettings);

             case WATERLOGGED_TALL_CROSS_4 -> new WaterloggedDoubleTallAgeCropBlock4(blockSettings);
         };

         Block cropBlock = registerBlockWithoutItem(name, ageCropBlock);

         if(hasSeeds) {
             seedsItem = registerItem(name + "_seeds", new SeedsItem(new FabricItemSettings(), cropBlock));
             cropItem = registerItem(name, new Item(RAW_EDIBLE_CROP_DEFAULT));
         } else {
             cropItem = registerItem(name, new SeedsItem(RAW_EDIBLE_CROP_DEFAULT, cropBlock));
         }

         cropFamilies.put(name, new CropFamily(name, seedsItem, cropItem, cropBlock, maxAge, hasSpecialDrops, hasSeeds, cropModelType, cropType));
     }

    public static CropFamily getCropFamily(String name) {
        return cropFamilies.get(name);
    }

    public static Map<String, CropFamily> getAllCrops() {
        return Collections.unmodifiableMap(cropFamilies);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(GrapeVine.MOD_ID, name), item);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(GrapeVine.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(GrapeVine.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(GrapeVine.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }
}
