package net.emilsg.grapevine.datagen;

import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagDataGen extends FabricTagProvider.BlockTagProvider {

    public BlockTagDataGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
            getOrCreateTagBuilder(BlockTags.CROPS).add(cropFamily.getCropBlock());
            getOrCreateTagBuilder(BlockTags.MAINTAINS_FARMLAND).add(cropFamily.getCropBlock());
        });

        getOrCreateTagBuilder(BlockTags.MAINTAINS_FARMLAND).add(ModRegistries.TRELLIS_FRAME);
        getOrCreateTagBuilder(BlockTags.CLIMBABLE).add(
                ModRegistries.TRELLIS_FRAME,
                ModRegistries.getCropFamily("red_grapes").getCropBlock(),
                ModRegistries.getCropFamily("green_grapes").getCropBlock()

        );
    }
}
