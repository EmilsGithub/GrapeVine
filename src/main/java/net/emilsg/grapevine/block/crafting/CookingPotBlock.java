package net.emilsg.grapevine.block.crafting;

import net.emilsg.grapevine.block.entity.HeatBlockEntity;
import net.emilsg.grapevine.screen.CookingScreenHandler;
import net.emilsg.grapevine.util.ModStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CookingPotBlock extends AbstractCookingBlock {
    private static final Text TITLE = Text.translatable("container.grapevine.cooking");

    public CookingPotBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected Identifier getInteractionStat() {
        return ModStats.INTERACT_WITH_COOKING_POT;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(2.5, 0, 2.5, 13.5, 11.5, 13.5);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new CookingScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HeatBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient && state.get(HEATED) ? HeatBlockEntity::clientTick : HeatBlockEntity::tick;
    }
}
