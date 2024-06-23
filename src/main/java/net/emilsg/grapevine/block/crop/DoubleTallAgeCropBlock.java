package net.emilsg.grapevine.block.crop;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public abstract class DoubleTallAgeCropBlock extends AgeCropBlock implements Fertilizable {
    private static final IntProperty AGE = Properties.AGE_4;
    private static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)};


    public DoubleTallAgeCropBlock(FabricBlockSettings settings, int maxAge) {
        super(settings, maxAge);
        this.setDefaultState((this.stateManager.getDefaultState()).with(HALF, DoubleBlockHalf.LOWER).with(getAgeProperty(), 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? VoxelShapes.fullCube() : AGE_TO_SHAPE[this.getAge(state)];
    }

    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else if(isLowerHalf(state) && !isUpperHalf(world.getBlockState(pos.up()))) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, HALF);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            if (player.isCreative()) {
                onBreakInCreative(world, pos, state, player);
            } else {
                TallPlantBlock.dropStacks(state, world, pos, null, player, player.getMainHandStack());
            }
        }
        super.onBreak(world, pos, state, player);
    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos blockPos;
        BlockState blockState;
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER && (blockState = world.getBlockState(blockPos = pos.down())).isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
            BlockState blockState2 = blockState.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER && !this.isMature(state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos below = pos.down();
        BlockPos above = pos.up();
        BlockState stateBelow = world.getBlockState(below);
        BlockState stateAbove = world.getBlockState(above);

        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return canPlantOnTop(stateBelow, world, below) && (world.isAir(above) || isUpperHalf(stateAbove));
        } else if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            return isLowerHalf(stateBelow);
        }
        return false;
    }


    private boolean isLowerHalf(BlockState state) {
        return state.isOf(this) && state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    private boolean isUpperHalf(BlockState state) {
        return state.isOf(this) && state.get(HALF) == DoubleBlockHalf.UPPER;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.FARMLAND);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean bl;
        float f = CropBlock.getAvailableMoisture(this, world, pos);
        boolean bl2 = bl = random.nextInt((int)(25.0f / f) + 1) == 0;
        if (bl) {
            this.tryGrow(world, state, pos, 1);
        }
    }

    private void tryGrow(ServerWorld world, BlockState state, BlockPos pos, int amount) {
        int i = Math.min(state.get(getAgeProperty()) + amount, 4);
        if (!this.canGrow(world, pos, state, i)) {
            return;
        }
        world.setBlockState(pos, state.with(getAgeProperty(), i), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.up(), withWaterloggedState(world, pos, (this.getDefaultState().with(getAgeProperty(), i)).with(HALF, DoubleBlockHalf.UPPER)), Block.NOTIFY_ALL);
    }

    public static BlockState withWaterloggedState(WorldView world, BlockPos pos, BlockState state) {
        if (state.contains(Properties.WATERLOGGED)) {
            return state.with(Properties.WATERLOGGED, world.isWater(pos));
        }
        return state;
    }

    private boolean canGrow(WorldView world, BlockPos pos, BlockState state, int age) {
        return this.getAge(state) < this.getMaxAge() && canPlaceAt(state, world, pos) && (age < getMaxAge() || canGrowAt(world, pos.up()));
    }

    private boolean canGrowAt(WorldView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.isOf(this);
    }

    @Nullable
    private LowerHalfContext getLowerHalfContext(WorldView world, BlockPos pos, BlockState state) {
        if (isLowerHalf(state)) {
            return new LowerHalfContext(pos, state);
        }
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (isLowerHalf(blockState)) {
            return new LowerHalfContext(blockPos, blockState);
        }
        return null;
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        LowerHalfContext lowerHalfContext = this.getLowerHalfContext(world, pos, state);
        if (lowerHalfContext == null) {
            return false;
        }
        return this.canGrow(world, lowerHalfContext.pos, lowerHalfContext.state, lowerHalfContext.state.get(getAgeProperty()) + 1);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        LowerHalfContext lowerHalfContext = this.getLowerHalfContext(world, pos, state);
        if (lowerHalfContext == null) {
            return;
        }
        this.tryGrow(world, lowerHalfContext.state, lowerHalfContext.pos, 1);
    }

    record LowerHalfContext(BlockPos pos, BlockState state) {
    }
}
