package net.emilsg.grapevine.item.items;

import net.emilsg.grapevine.block.crop.AgeCropBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SeedsItem extends AliasedBlockItem {

    public SeedsItem(Settings settings, Block block) {
        super(block, settings);
        if(block instanceof AgeCropBlock ageCropBlock) {
            ageCropBlock.setSeedsItem(this.asItem());
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof FarmlandBlock && context.getSide() == Direction.UP && world.getBlockState(pos.up()).isReplaceable()) {
            return super.useOnBlock(context);
        }

        return ActionResult.FAIL;
    }
}
