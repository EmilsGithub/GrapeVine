package net.emilsg.grapevine.datagen;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.block.crop.AgeCropBlock;
import net.emilsg.grapevine.block.crop.CropModelType;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModelDataGen extends FabricModelProvider {
    public static final TextureKey CROP = TextureKey.of("cross_crop");
    public static final Model CROSS_CROP = block("cross_crop", CROP);


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

    private static Model block(String parent, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier(GrapeVine.MOD_ID, "block/parent/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    public static TextureMap crossCrop(Identifier cropID) {
        return new TextureMap().put(CROP, cropID);
    }

}
