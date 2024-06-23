package net.emilsg.grapevine.block.crop;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class DoubleTallAgeCropBlock4 extends DoubleTallAgeCropBlock {
    private static final IntProperty AGE = Properties.AGE_4;
    private static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    public DoubleTallAgeCropBlock4(FabricBlockSettings settings) {
        super(settings, 4);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }
}
