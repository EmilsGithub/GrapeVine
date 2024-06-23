package net.emilsg.grapevine.block.entity;

import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<HeatBlockEntity> HEAT_BLOCK_ENTITY;

    public static void registerModBlockEntities() {
        HEAT_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(GrapeVine.MOD_ID, "heat"),
                FabricBlockEntityTypeBuilder.create(HeatBlockEntity::new,
                        ModRegistries.FRYING_PAN,
                        ModRegistries.COOKING_POT
                ).build());
    }
}
