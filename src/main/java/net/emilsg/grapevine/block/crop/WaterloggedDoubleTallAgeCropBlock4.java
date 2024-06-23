package net.emilsg.grapevine.block.crop;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class WaterloggedDoubleTallAgeCropBlock4 extends DoubleTallAgeCropBlock implements Waterloggable {
    private static final IntProperty AGE = Properties.AGE_4;
    private static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public WaterloggedDoubleTallAgeCropBlock4(FabricBlockSettings settings) {
        super(settings, 4);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        boolean topAndWater = state.get(HALF) == DoubleBlockHalf.UPPER && world.getBlockState(pos).getFluidState().isOf(Fluids.WATER);
        return super.canPlaceAt(state, world, pos) && !topAndWater;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(BlockTags.DIRT);
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF, WATERLOGGED);
    }
}
