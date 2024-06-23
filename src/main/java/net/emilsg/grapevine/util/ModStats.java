package net.emilsg.grapevine.util;

import net.emilsg.grapevine.GrapeVine;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class ModStats {

    public static final Identifier INTERACT_WITH_FRYING_PAN = register("interact_with_frying_pan", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_COOKING_POT = register("interact_with_cooking_pot", StatFormatter.DEFAULT);


    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(GrapeVine.MOD_ID, id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    public static void registerModStats() {

    }
}
