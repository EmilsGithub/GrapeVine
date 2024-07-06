package net.emilsg.grapevine.datagen;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.block.crop.AgeCropBlock;
import net.emilsg.grapevine.block.crop.CropModelType;
import net.emilsg.grapevine.block.crop.TrellisCropBlock;
import net.emilsg.grapevine.block.crop.TrellisCropBlock4;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class ModelDataGen extends FabricModelProvider {
    public static final TextureKey CROP = TextureKey.of("cross_crop");
    public static final Model CROSS_CROP = block("cross_crop", CROP);

    public static final TextureKey INNER = TextureKey.of("inner");
    public static final TextureKey OUTER = TextureKey.of("outer");
    public static final Model CROP_TRELLIS = block("crop_trellis", INNER, OUTER);


    public ModelDataGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
            if (cropFamily.getCropBlock() instanceof AgeCropBlock) {
                IntProperty ageProperty = ((AgeCropBlock) cropFamily.getCropBlock()).getAgeProperty();
                registerDynamicCrop(generator, cropFamily.getCropBlock(), ageProperty, cropFamily.getCropModelType());
            }
        });

        TrellisCropBlock4 greenGrapes = (TrellisCropBlock4) ModRegistries.getAllCrops().get("green_grapes").getCropBlock();
        registerTrellisCrop(generator, greenGrapes, Properties.AGE_4);
        TrellisCropBlock4 redGrapes = (TrellisCropBlock4) ModRegistries.getAllCrops().get("red_grapes").getCropBlock();
        registerTrellisCrop(generator, redGrapes, Properties.AGE_4);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModRegistries.KNIFE, Models.HANDHELD);
        generator.register(ModRegistries.BUTCHER_KNIFE, Models.HANDHELD);
        generator.register(ModRegistries.FRYING_PAN.asItem(), Models.HANDHELD);

        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
            if(cropFamily.getHasSeeds() && cropFamily.getSeedsItem() != null) {
                registerCropAndSeeds(generator, cropFamily.getCropItem(), cropFamily.getSeedsItem());
            } else {
                generator.register(cropFamily.getCropItem(), Models.GENERATED);
            }
        });
    }

     /** Custom Generators **/

    private void registerCropAndSeeds(ItemModelGenerator generator, Item crop, Item seeds) {
        generator.register(crop, Models.GENERATED);
        generator.register(seeds, Models.GENERATED);
    }

    private void registerDynamicCrop(BlockStateModelGenerator generator, Block cropBlock, IntProperty ageProperty, CropModelType cropModelType) {
        switch (cropModelType) {
            case CROP -> registerCrop(generator, cropBlock, ageProperty);
            case CROSS -> registerCrossCrop(generator, cropBlock, ageProperty);
            case TALL_CROP -> registerTallCrop(generator, cropBlock, ageProperty);
            case TALL_CROSS -> registerTallCrossCrop(generator, cropBlock, ageProperty);
        }
    }

    private void registerCrop(BlockStateModelGenerator generator, Block crop, IntProperty ageProperty) {
        Int2ObjectMap<Identifier> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty).register((integer) -> {
            Identifier identifier = int2ObjectMap.computeIfAbsent(integer, (i) -> {
                return generator.createSubModel(crop, "_stage_" + i, Models.CROP, TextureMap::crop);
            });
            return BlockStateVariant.create().put(VariantSettings.MODEL, identifier);
        });
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(blockStateVariantMap));
    }

    private void registerTallCrop(BlockStateModelGenerator generator, Block crop, IntProperty ageProperty) {
        Int2ObjectMap<Identifier> topModels = new Int2ObjectOpenHashMap<>();
        Int2ObjectMap<Identifier> bottomModels = new Int2ObjectOpenHashMap<>();

        BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty, Properties.DOUBLE_BLOCK_HALF).register((age, half) -> {
            Identifier topID = topModels.computeIfAbsent(age, (i) -> {
                return generator.createSubModel(crop, "_stage_" + i + "_top", Models.CROP, TextureMap::crop);
            });
            Identifier bottomID = bottomModels.computeIfAbsent(age, (i) -> {
                return generator.createSubModel(crop, "_stage_" + i + "_bottom", Models.CROP, TextureMap::crop);
            });

            return switch (half) {
                case UPPER -> BlockStateVariant.create().put(VariantSettings.MODEL, topID);
                case LOWER -> BlockStateVariant.create().put(VariantSettings.MODEL, bottomID);
            };
        });

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(blockStateVariantMap));
    }

    private void registerTallCrossCrop(BlockStateModelGenerator generator, Block crop, IntProperty ageProperty) {
        Int2ObjectMap<Identifier> topModels = new Int2ObjectOpenHashMap<>();
        Int2ObjectMap<Identifier> bottomModels = new Int2ObjectOpenHashMap<>();

        BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty, Properties.DOUBLE_BLOCK_HALF).register((age, half) -> {
            Identifier topID = topModels.computeIfAbsent(age, (i) -> {
                return generator.createSubModel(crop, "_stage_" + i + "_top", CROSS_CROP, ModelDataGen::crossCrop);
            });
            Identifier bottomID = bottomModels.computeIfAbsent(age, (i) -> {
                return generator.createSubModel(crop, "_stage_" + i + "_bottom", CROSS_CROP, ModelDataGen::crossCrop);
            });

            return switch (half) {
                case UPPER -> BlockStateVariant.create().put(VariantSettings.MODEL, topID);
                case LOWER -> BlockStateVariant.create().put(VariantSettings.MODEL, bottomID);
            };
        });

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(blockStateVariantMap));
    }

    private void registerCrossCrop(BlockStateModelGenerator generator, Block crop, IntProperty ageProperty) {
        Int2ObjectMap<Identifier> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty).register((integer) -> {
            Identifier identifier = int2ObjectMap.computeIfAbsent(integer, (i) -> {
                return generator.createSubModel(crop, "_stage_" + i, CROSS_CROP, ModelDataGen::crossCrop);
            });
            return BlockStateVariant.create().put(VariantSettings.MODEL, identifier);
        });
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(blockStateVariantMap));
    }

    private void registerTrellisCrop(BlockStateModelGenerator generator, TrellisCropBlock trellisBlock, IntProperty ageProperty) {
        Int2ObjectMap<Identifier> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty, Properties.HORIZONTAL_FACING).register((age, facing) -> {
            Identifier identifier = int2ObjectMap.computeIfAbsent(age, (i) -> {
                return generator.createSubModel(trellisBlock, "_stage_" + i, CROP_TRELLIS, ModelDataGen::cropTrellis);
            });
            return BlockStateVariant.create()
                    .put(VariantSettings.MODEL, identifier)
                    .put(VariantSettings.Y, facing == Direction.NORTH ? VariantSettings.Rotation.R0 : facing == Direction.EAST ? VariantSettings.Rotation.R90 : facing == Direction.SOUTH ? VariantSettings.Rotation.R180 : VariantSettings.Rotation.R270);  // Add Y rotation based on facing direction
        });
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(trellisBlock).coordinate(blockStateVariantMap));
    }

    private static Model block(String parent, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier(GrapeVine.MOD_ID, "block/parent/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    public static TextureMap crossCrop(Identifier cropID) {
        return new TextureMap().put(CROP, cropID);
    }

    private static TextureMap cropTrellis(Identifier trellisBlockID) {
        Identifier inner = new Identifier(GrapeVine.MOD_ID, trellisBlockID.getPath() + "_inner");
        Identifier outer = new Identifier(GrapeVine.MOD_ID, trellisBlockID.getPath() + "_outer");
        return new TextureMap().put(INNER, inner).put(OUTER, outer);
    }

}
