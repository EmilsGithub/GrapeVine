package net.emilsg.grapevine.block.crop;

import net.emilsg.grapevine.block.ICutOut;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class TrellisFrameBlock extends HorizontalFacingBlock implements ICutOut {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private final VoxelShape NS_SHAPE = Block.createCuboidShape(0, 0, 6, 16, 16, 10);
    private final VoxelShape EW_SHAPE = Block.createCuboidShape(6, 0, 0, 10, 16, 16);

    public TrellisFrameBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> NS_SHAPE;
            case EAST, WEST -> EW_SHAPE;
            default -> VoxelShapes.empty();
        };    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }
}
