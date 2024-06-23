package net.emilsg.grapevine.block;

import net.emilsg.grapevine.GrapeVine;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags {
    public static final TagKey<Block> STOVEISH_BLOCKS = create("stoveish_blocks");

    private static TagKey<Block> create(String path) {
        return create(path, GrapeVine.MOD_ID);
    }

    private static TagKey<Block> create(String path, String namespace) {
        return TagKey.of(Registries.BLOCK.getKey(), new Identifier(namespace, path));
    }
}
