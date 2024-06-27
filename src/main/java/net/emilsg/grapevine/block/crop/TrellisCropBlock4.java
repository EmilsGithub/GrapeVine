package net.emilsg.grapevine.block.crop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;

public class TrellisCropBlock4 extends TrellisCropBlock {
    public static final IntProperty AGE = Properties.AGE_4;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;


    public TrellisCropBlock4(Settings settings, Item cropItem, Item seedsItem) {
        super(settings, cropItem, seedsItem);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, FACING);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }
}
