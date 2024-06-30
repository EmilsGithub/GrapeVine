package net.emilsg.grapevine.block.crop;

import net.minecraft.item.Item;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class TrellisCropBlock4 extends TrellisCropBlock {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public TrellisCropBlock4(Settings settings, Item cropItem, Item seedsItem) {
        super(settings, cropItem, seedsItem);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH).with(getAgeProperty(), 0));
    }

    @Override
    public IntProperty getAgeProperty() {
        return Properties.AGE_4;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }
}
