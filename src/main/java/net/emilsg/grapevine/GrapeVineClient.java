package net.emilsg.grapevine;

import net.emilsg.grapevine.block.ICutOut;
import net.emilsg.grapevine.register.ModRegistries;
import net.emilsg.grapevine.screen.CookingScreen;
import net.emilsg.grapevine.screen.FryingScreen;
import net.emilsg.grapevine.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;

public class GrapeVineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (Block block : Registries.BLOCK) {
            if (block instanceof ICutOut) BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        }

        registerScreenHandlers();
        registerColorProviders();
    }

    private void registerScreenHandlers() {
        HandledScreens.register(ModScreenHandlers.FRYING, FryingScreen::new);
        HandledScreens.register(ModScreenHandlers.COOKING, CookingScreen::new);
    }

    private void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        (world != null && pos != null) ? BiomeColors.getWaterColor(world, pos) : 255,
                ModRegistries.COOKING_POT

        );
    }

}
