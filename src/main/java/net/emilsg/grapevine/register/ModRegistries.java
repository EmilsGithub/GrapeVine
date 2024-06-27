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

    private static final FabricBlockSettings BASE_TRELLIS_BROWN = FabricBlockSettings.create().ticksRandomly().sounds(BlockSoundGroup.VINE).pistonBehavior(PistonBehavior.DESTROY).mapColor(MapColor.BROWN);

    private static final Map<String, CropFamily> cropFamilies = new HashMap<>();

    public static final Item KNIFE = registerItem("knife", new Item(new FabricItemSettings()));
    public static final Item BUTCHER_KNIFE = registerItem("butcher_knife", new Item(new FabricItemSettings()));

    public static final Block FRYING_PAN = registerBlock("frying_pan", new FryingPanBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).breakInstantly()));
    public static final Block COOKING_POT = registerBlock("cooking_pot", new CookingPotBlock(FabricBlockSettings.copy(Blocks.IRON_BLOCK).breakInstantly()));
    public static final Block TRELLIS_FRAME = registerBlock("trellis_frame", new TrellisFrameBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));

    public static void initRegistriesAndCrops() {
        registerCrop("lettuce", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEAFY_GREENS);
        registerCrop("cabbage", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEAFY_GREENS);
        registerCrop("corn", 3, CropModelType.TALL_CROSS, BASE_CROP_GREEN, false, true, CropType.GRAINS);
        registerCrop("garlic", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.BULBS);
        registerCrop("onion", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.BULBS);
        registerCrop("rice", 4, CropModelType.WATERLOGGED_TALL_CROSS, BASE_CROP_GREEN, false, true, CropType.GRAINS);
        registerCrop("chili", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("bell_pepper", 4, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("tomato", 4, CropModelType.TALL_CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("eggplant", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("long_beans", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCrop("peanut", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCrop("basil", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.HERBS);
        registerCrop("mint", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.HERBS);
        registerCrop("peas", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCrop("bean_sprout", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("spring_onion", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.BULBS);
        registerCrop("sweet_potato", 3, CropModelType.CROP, BASE_CROP_GREEN, false, false, CropType.TUBERS);
        registerCrop("spinach", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEAFY_GREENS);
        registerCrop("mustard", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("soy_bean", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, CropType.LEGUMES);
        registerCrop("ginger", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);
        registerCrop("pepper", 3, CropModelType.CROP, BASE_CROP_GREEN, false, true, null);

        registerTrellisCrop("grapes", 4, BASE_TRELLIS_BROWN, true, CropType.FRUITS);
    }

    /**
     Helper Methods
     **/

    private static void registerTrellisCrop(String name, int maxAge, FabricBlockSettings defaultBlockSetting, boolean shouldMakeSeeds, CropType cropType) {
        Block cropBlock;
        Item seedsItem = null;
        Item cropItem;

        String seedName = name.endsWith("s") ? name.substring(0, name.length() - 1) : name;

        if(shouldMakeSeeds) seedsItem = registerItem(seedName + "_seeds", new Item(new FabricItemSettings()));
        cropItem = registerItem(name, new Item(RAW_EDIBLE_CROP_DEFAULT));

        cropBlock = switch (maxAge) {
            default -> new TrellisCropBlock4(defaultBlockSetting, cropItem, seedsItem);
        };

        cropBlock = registerBlock(seedName + "_trellis", cropBlock);

        cropFamilies.put(name, new CropFamily(name, seedsItem, cropItem, cropBlock, maxAge, true, shouldMakeSeeds, CropModelType.TRELLIS, cropType, true));
    }

     public static void registerCrop(String name, int maxAge, CropModelType cropModelType, FabricBlockSettings blockSettings, boolean hasSpecialDrops, boolean hasSeeds, @Nullable CropType cropType) {
         Block cropBlock;
         Item seedsItem = null;
         Item cropItem;

         cropBlock = switch (maxAge) {
             case 3 -> switch (cropModelType) {
                 case CROP, CROSS -> new AgeCropBlock3(blockSettings);
                 case TALL_CROP, TALL_CROSS -> new DoubleTallAgeCropBlock3(blockSettings);
                 default -> null;
             };
             case 4 -> switch (cropModelType) {
                 case CROP, CROSS -> new AgeCropBlock4(blockSettings);
                 case TALL_CROP, TALL_CROSS -> new DoubleTallAgeCropBlock4(blockSettings);
                 case WATERLOGGED_TALL_CROSS -> new WaterloggedDoubleTallAgeCropBlock4(blockSettings);
                 default -> null;
             };
             case 5 -> switch (cropModelType) {
                 case CROP, CROSS -> new AgeCropBlock5(blockSettings);
                 case TALL_CROP, TALL_CROSS -> new DoubleTallAgeCropBlock5(blockSettings);
                 default -> null;
             };
             default -> null;
         };

         cropBlock = registerBlockWithoutItem(name, cropBlock);

         if(hasSeeds) {
             seedsItem = registerItem(name + "_seeds", new SeedsItem(new FabricItemSettings(), cropBlock));
             cropItem = registerItem(name, new Item(RAW_EDIBLE_CROP_DEFAULT));
         } else {
             cropItem = registerItem(name, new SeedsItem(RAW_EDIBLE_CROP_DEFAULT, cropBlock));
         }

         cropFamilies.put(name, new CropFamily(name, seedsItem, cropItem, cropBlock, maxAge, hasSpecialDrops, hasSeeds, cropModelType, cropType, false));
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
