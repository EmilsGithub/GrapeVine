package net.emilsg.grapevine.util;

import net.emilsg.grapevine.mixin.ItemAccessor;
import net.emilsg.grapevine.register.ModRegistries;

public class ModUtil {

    public static void registerModUtil() {
        registerUtensils();
    }

    public static void registerUtensils() {
        ((ItemAccessor) ModRegistries.KNIFE).setRecipeRemainder(ModRegistries.KNIFE);
        ((ItemAccessor) ModRegistries.BUTCHER_KNIFE).setRecipeRemainder(ModRegistries.BUTCHER_KNIFE);

    }
}
