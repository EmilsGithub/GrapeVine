package net.emilsg.grapevine.block.crop;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class AgeCropBlock4 extends AgeCropBlock{
    private static final IntProperty AGE = Properties.AGE_4;

    public AgeCropBlock4(FabricBlockSettings settings) {
        super(settings, 4);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
