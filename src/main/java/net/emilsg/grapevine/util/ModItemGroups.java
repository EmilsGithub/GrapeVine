package net.emilsg.grapevine.util;

import net.emilsg.grapevine.GrapeVine;
import net.emilsg.grapevine.register.ModRegistries;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup GRAPEVINE = Registry.register(Registries.ITEM_GROUP,
            new Identifier(GrapeVine.MOD_ID, "grapevine"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.grapevine"))
                    .icon(() -> new ItemStack(ModRegistries.getCropFamily("tomato").getCropItem()))
                    .entries((displayContext, entries) -> {

                        entries.add(ModRegistries.KNIFE);
                        entries.add(ModRegistries.BUTCHER_KNIFE);

                        entries.add(ModRegistries.FRYING_PAN);
                        entries.add(ModRegistries.COOKING_POT);

                        entries.add(ModRegistries.TRELLIS_FRAME);

                        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
                            if(cropFamily.shouldAddCropBlockToGroup()) entries.add(new ItemStack(cropFamily.getCropBlock()));
                        });
                        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
                            if(cropFamily.getCropItem() != null) entries.add(new ItemStack(cropFamily.getCropItem()));
                        });
                        ModRegistries.getAllCrops().values().forEach(cropFamily -> {
                            if(cropFamily.getHasSeeds() && cropFamily.getSeedsItem() != null) entries.add(new ItemStack(cropFamily.getSeedsItem()));
                        });

                    }).build());

    public static void registerItemGroups() {

    }
}
