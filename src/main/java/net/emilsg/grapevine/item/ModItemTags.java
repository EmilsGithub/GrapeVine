package net.emilsg.grapevine.item;

import net.emilsg.grapevine.GrapeVine;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {
    public static final TagKey<Item> BULBS = create("bulbs");
    public static final TagKey<Item> GRAINS = create("grains");
    public static final TagKey<Item> FRUITS = create("fruits");
    public static final TagKey<Item> HERBS = create("herbs");
    public static final TagKey<Item> LEAFY_GREENS = create("leafy_greens");
    public static final TagKey<Item> LEGUMES = create("legumes");
    public static final TagKey<Item> TUBERS = create("tubers");

    private static TagKey<Item> create(String path) {
        return create(path, GrapeVine.MOD_ID);
    }

    private static TagKey<Item> create(String path, String namespace) {
        return TagKey.of(Registries.ITEM.getKey(), new Identifier(namespace, path));
    }
}
