package net.emilsg.grapevine.block.entity;

import net.emilsg.grapevine.block.crafting.CookingPotBlock;
import net.emilsg.grapevine.block.crafting.FryingPanBlock;
import net.emilsg.grapevine.block.ModBlockTags;
import net.emilsg.grapevine.util.ModProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HeatBlockEntity extends BlockEntity {

    public HeatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HEAT_BLOCK_ENTITY, pos, state);
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        boolean heated = world.getBlockState(pos.down()).isIn(ModBlockTags.STOVEISH_BLOCKS) || (world.getBlockState(pos.down()).getBlock() instanceof FurnaceBlock && world.getBlockState(pos.down()).get(Properties.LIT));
        world.setBlockState(pos, state.with(ModProperties.HEATED, heated), Block.NOTIFY_ALL);
    }


    public static <T extends BlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T t) {
        if(state.getBlock() instanceof FryingPanBlock) {
            Random random = world.getRandom();
            if (random.nextInt(12) == 0) world.addParticle(ParticleTypes.SMALL_FLAME, pos.getX() + 0.5 + (random.nextFloat() / 5 * (random.nextBoolean() ? -1 : 1)), pos.getY() + 0.125f, pos.getZ() + 0.5 + (random.nextFloat() / 5 * (random.nextBoolean() ? -1 : 1)), random.nextDouble() * 0.005f * (random.nextBoolean() ? -1 : 1), 0.001f, random.nextDouble() * 0.005f * (random.nextBoolean() ? -1 : 1));
        }

        if(state.getBlock() instanceof CookingPotBlock) {
            Random random = world.getRandom();
            if (random.nextInt(12) == 0)
                world.addParticle(ParticleTypes.BUBBLE_POP, pos.getX() + 0.5 + (random.nextFloat() / 6 * (random.nextBoolean() ? -1 : 1)), pos.getY() + 0.625, pos.getZ() + 0.5 + (random.nextFloat() / 6 * (random.nextBoolean() ? -1 : 1)), random.nextDouble() * 0.04f - 0.02f, 0.02f, random.nextDouble() * 0.04f - 0.02f);

        }
    }
}
