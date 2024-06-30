package net.emilsg.grapevine.block.crop;

import net.emilsg.grapevine.register.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public abstract class TrellisCropBlock extends TrellisFrameBlock {
    private final Item cropItem;
    private final Item seedsItem;

    public TrellisCropBlock(Settings settings, Item cropItem, @Nullable Item seedsItem) {
        super(settings);
        this.cropItem = cropItem;
        if (seedsItem != null) this.seedsItem = seedsItem;
        else this.seedsItem = cropItem;
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(getAgeProperty(), FACING);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(getAgeProperty());
        BlockState upState = world.getBlockState(pos.up());
        if (random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            if (age >= 2 && upState.isOf(ModRegistries.TRELLIS_FRAME)) {
                world.setBlockState(pos.up(), this.getDefaultState().with(FACING, upState.get(FACING)), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.up(), GameEvent.Emitter.of(this.getDefaultState().with(FACING, upState.get(FACING))));
            }
            if (age < getMaxAge()) {
                BlockState agedState = state.with(getAgeProperty(), age + 1);
                world.setBlockState(pos, agedState, Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(agedState));
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stackInHand = player.getStackInHand(hand);

        if(stackInHand.getItem() instanceof ShearsItem) {
            this.removeCrop(world, pos, state);
            stackInHand.damage(1, player, p -> player.sendToolBreakStatus(hand));
            world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS);
            return ActionResult.success(world.isClient);
        } else if (stackInHand.isEmpty() && state.get(getAgeProperty()) == getMaxAge()) {
            world.playSound(null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS);
            this.pickGrapes(world, pos, state);
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    public void removeCrop(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(TrellisFrameBlock.FACING);
        int age = state.get(getAgeProperty());
        ItemStack droppedCropStack;
        ItemStack droppedSeedStack = new ItemStack(getSeedItem());
        if (age == 0) {
            droppedCropStack = null;
        } else {
            droppedCropStack = new ItemStack(getCropItem());
            droppedCropStack.setCount(world.random.nextBetweenExclusive(1, Math.min(age + 1, 3)));
        }

        world.setBlockState(pos, ModRegistries.TRELLIS_FRAME.getDefaultState().with(TrellisFrameBlock.FACING, facing), Block.NOTIFY_ALL);
        if(droppedCropStack != null) TrellisCropBlock.dropStack(world, pos, droppedCropStack);
        TrellisCropBlock.dropStack(world, pos, droppedSeedStack);
    }

    public void pickGrapes(World world, BlockPos pos, BlockState state) {
        ItemStack droppedCropStack = new ItemStack(getCropItem());
        droppedCropStack.setCount(world.random.nextInt(3) + 1);
        world.setBlockState(pos, state.with(this.getAgeProperty(), 0), Block.NOTIFY_ALL);
        TrellisCropBlock.dropStack(world, pos, droppedCropStack);
    }

    public Item getCropItem() {
        return cropItem;
    }

    public Item getSeedItem() {
        return seedsItem;
    }

    public abstract int getMaxAge();

    public abstract IntProperty getAgeProperty();
}
