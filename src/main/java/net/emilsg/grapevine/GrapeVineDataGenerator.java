package net.emilsg.grapevine;

import net.emilsg.grapevine.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class GrapeVineDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ItemTagDataGen::new);
		pack.addProvider(BlockTagDataGen::new);
		pack.addProvider(LootTableDataGen::new);
		pack.addProvider(ModelDataGen::new);
		pack.addProvider(RecipeDataGen::new);
		pack.addProvider(ModPOITagDataGen::new);
	}
}
