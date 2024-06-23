package net.emilsg.grapevine.block.crop;

import net.emilsg.grapevine.block.ICutOut;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class AgeCropBlock extends CropBlock implements ICutOut {
    private final int maxAge;
    private Item seedsItem = null;

    public AgeCropBlock(FabricBlockSettings settings, int maxAge) {
        super(settings);
        this.maxAge = maxAge;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float f;
        int i;
        if (world.getBaseLightLevel(pos, 0) >= 9 && (i = this.getAge(state)) < this.getMaxAge() && random.nextInt((int)((35.0f - maxAge) / (f = CropBlock.getAvailableMoisture(this, world, pos))) + 1) == 0) {
            world.setBlockState(pos, this.withAge(i + 1), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 1, maxAge -1);
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return seedsItem;
    }

    public void setSeedsItem(Item seedsItem) {
        this.seedsItem = seedsItem;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public abstract IntProperty getAgeProperty();

    @Override
    public abstract void appendProperties(StateManager.Builder<Block, BlockState> builder);
}
