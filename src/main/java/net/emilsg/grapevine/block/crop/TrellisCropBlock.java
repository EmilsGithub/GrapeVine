package net.emilsg.grapevine.block.crop;

import net.emilsg.grapevine.register.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class TrellisCropBlock extends TrellisFrameBlock {
    private final Item cropItem;
    private final Item seedsItem;

    public TrellisCropBlock(Settings settings, Item cropItem, @Nullable Item seedsItem) {
        super(settings);
        this.cropItem = cropItem;
        if (seedsItem != null) this.seedsItem = seedsItem;
        else this.seedsItem = cropItem;
    }

    @Override
    protected abstract void appendProperties(StateManager.Builder<Block, BlockState> builder);

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack activeItem = player.getActiveItem();

        if(activeItem.getItem() instanceof ShearsItem) {
            this.removeCrop(world, pos, state);
            activeItem.damage(1, player, p -> player.sendToolBreakStatus(hand));
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    public void removeCrop(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
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

    public Item getCropItem() {
        return cropItem;
    }

    public Item getSeedItem() {
        return seedsItem;
    }

    public abstract int getMaxAge();

    public abstract IntProperty getAgeProperty();
}
