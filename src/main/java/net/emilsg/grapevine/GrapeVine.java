package net.emilsg.grapevine;

import net.emilsg.grapevine.block.entity.ModBlockEntities;
import net.emilsg.grapevine.recipe.ModRecipeSerializers;
import net.emilsg.grapevine.register.ModRegistries;
import net.emilsg.grapevine.screen.ModScreenHandlers;
import net.emilsg.grapevine.util.ModItemGroups;
import net.emilsg.grapevine.util.ModStats;
import net.emilsg.grapevine.util.ModUtil;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrapeVine implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("grapevine");
	public static final String MOD_ID = "grapevine";

	@Override
	public void onInitialize() {
		ModRegistries.initRegistriesAndCrops();
		ModItemGroups.registerItemGroups();
		ModBlockEntities.registerModBlockEntities();

		ModStats.registerModStats();

		ModRecipeSerializers.registerRecipeSerializers();

		ModScreenHandlers.registerScreenHandlers();
		ModUtil.registerModUtil();
	}
}