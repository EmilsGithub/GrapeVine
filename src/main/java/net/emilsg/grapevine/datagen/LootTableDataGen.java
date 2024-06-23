package net.emilsg.grapevine.datagen;

import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.block.crop.DoubleTallAgeCropBlock;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class LootTableDataGen extends FabricBlockLootTableProvider {

    public LootTableDataGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
            if (cropFamily.getHasSpecialDrops()) return;
            Block cropBlock = cropFamily.getCropBlock();
            Item cropItem = cropFamily.getCropItem();
            Item seedsItem = cropFamily.getSeedsItem();
            int maxAge = cropFamily.getMaxAge();

            IntProperty ageProperty = switch (maxAge) {
                case 5 -> Properties.AGE_5;
                case 4 -> Properties.AGE_4;
                default -> Properties.AGE_3;
            };

            BlockStatePropertyLootCondition.Builder ageConditionBuilder = BlockStatePropertyLootCondition.builder(cropBlock)
                    .properties(StatePredicate.Builder.create().exactMatch(ageProperty, maxAge));

            BlockStatePropertyLootCondition.Builder ageAndHalfConditionBuilder = BlockStatePropertyLootCondition.builder(cropBlock)
                    .properties(StatePredicate.Builder.create().exactMatch(ageProperty, maxAge).exactMatch(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));

            BlockStatePropertyLootCondition.Builder blockHalfConditionBuilder = BlockStatePropertyLootCondition.builder(cropBlock)
                    .properties(StatePredicate.Builder.create().exactMatch(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));

            if(cropBlock instanceof DoubleTallAgeCropBlock) addDrop(cropBlock, seedsItem != null ? tallCropDrops(cropBlock, cropItem, seedsItem, blockHalfConditionBuilder, ageAndHalfConditionBuilder) : noSeedsTallCropDrops(cropBlock, cropItem, blockHalfConditionBuilder, ageAndHalfConditionBuilder));
            else addDrop(cropBlock, seedsItem != null ? cropDrops(cropBlock, cropItem, seedsItem, ageConditionBuilder) : noSeedsCropDrops(cropBlock, cropItem, ageConditionBuilder));
        });
    }

    public LootTable.Builder cropDrops(Block crop, Item product, Item seeds, LootCondition.Builder condition) {
        return this.applyExplosionDecay(crop, LootTable.builder()
                .pool(LootPool.builder()
                        .with((ItemEntry.builder(product).apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 1)).conditionally(condition))
                                .alternatively(ItemEntry.builder(seeds))))
                .pool(LootPool.builder().conditionally(condition)
                        .with((ItemEntry.builder(seeds)
                                .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 2))))))
                .randomSequenceId(new Identifier(GrapeVine.MOD_ID, Registries.BLOCK.getId(crop).getPath()));
    }

    public LootTable.Builder noSeedsCropDrops(Block crop, Item product, LootCondition.Builder condition) {
        return this.applyExplosionDecay(crop, LootTable.builder()
                .pool(LootPool.builder()
                        .with(ItemEntry.builder(product)))
                .pool(LootPool.builder().conditionally(condition)
                        .with((ItemEntry.builder(product)
                                .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 2))))))
                .randomSequenceId(new Identifier(GrapeVine.MOD_ID, Registries.BLOCK.getId(crop).getPath()));
    }

    public LootTable.Builder tallCropDrops(Block tallCropBlock, Item product, Item seeds, LootCondition.Builder halfCondition, LootCondition.Builder ageAndHalfCondition) {
        return this.applyExplosionDecay(tallCropBlock, LootTable.builder()
                        .pool(LootPool.builder()
                                .with((ItemEntry.builder(product).apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 1)).conditionally(ageAndHalfCondition))
                                        .alternatively(ItemEntry.builder(seeds).conditionally(halfCondition))))
                        .pool(LootPool.builder().conditionally(ageAndHalfCondition)
                                .with((ItemEntry.builder(seeds)
                                        .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 2))))))
                .randomSequenceId(new Identifier(GrapeVine.MOD_ID, Registries.BLOCK.getId(tallCropBlock).getPath()));
    }

    public LootTable.Builder noSeedsTallCropDrops(Block tallCropBlock, Item product, LootCondition.Builder halfCondition, LootCondition.Builder ageAndHalfCondition) {
        return this.applyExplosionDecay(tallCropBlock, LootTable.builder()
                        .pool(LootPool.builder()
                                .with((ItemEntry.builder(product).conditionally(halfCondition))))
                        .pool(LootPool.builder().conditionally(ageAndHalfCondition)
                                .with((ItemEntry.builder(product)
                                        .apply(ApplyBonusLootFunction.binomialWithBonusCount(Enchantments.FORTUNE, 0.5714286f, 2))))))
                .randomSequenceId(new Identifier(GrapeVine.MOD_ID, Registries.BLOCK.getId(tallCropBlock).getPath()));
    }
}
