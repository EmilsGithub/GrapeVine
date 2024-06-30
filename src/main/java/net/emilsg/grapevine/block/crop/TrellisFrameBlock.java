package net.emilsg.grapevine.block.crop;

import net.emilsg.grapevine.block.ICutOut;
import net.emilsg.grapevine.register.ModRegistries;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrellisFrameBlock extends HorizontalFacingBlock implements ICutOut {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private final VoxelShape NS_SHAPE = Block.createCuboidShape(0, 0, 6, 16, 16, 10);
    private final VoxelShape EW_SHAPE = Block.createCuboidShape(6, 0, 0, 10, 16, 16);

    public TrellisFrameBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> NS_SHAPE;
            case EAST, WEST -> EW_SHAPE;
            default -> VoxelShapes.empty();
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stackInHand = player.getStackInHand(hand);

        if(stackInHand.isOf(ModRegistries.getCropFamily("grapes").getSeedsItem()) && (world.getBlockState(pos.down()).isOf(ModRegistries.getCropFamily("grapes").getCropBlock()) || world.getBlockState(pos.down()).isOf(Blocks.FARMLAND))) {
            world.setBlockState(pos, ModRegistries.getCropFamily("grapes").getCropBlock().getDefaultState().with(FACING, state.get(FACING)));
            world.playSound(null, pos, SoundEvents.BLOCK_VINE_PLACE, SoundCategory.BLOCKS);
            if(!player.getAbilities().creativeMode) {
                stackInHand.decrement(1);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

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
