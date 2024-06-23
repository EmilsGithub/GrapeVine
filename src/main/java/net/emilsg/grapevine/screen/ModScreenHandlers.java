package net.emilsg.grapevine.screen;

import net.emilsg.grapevine.GrapeVine;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static final ScreenHandlerType<FryingScreenHandler> FRYING = register("frying", FryingScreenHandler::new);
    public static final ScreenHandlerType<CookingScreenHandler> COOKING = register("cooking", CookingScreenHandler::new);


    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(GrapeVine.MOD_ID, id), new ScreenHandlerType<T>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerScreenHandlers() {

    }
}
