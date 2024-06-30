package net.emilsg.grapevine.mixin;

import net.emilsg.grapevine.block.crop.TrellisFrameBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {

    @Inject(method = "canPlaceAt", at = @At(value = "RETURN"), cancellable = true)
    private void canPlaceAtTrellis(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if(world.getBlockState(pos.up()).getBlock() instanceof TrellisFrameBlock) cir.setReturnValue(true);
    }
}
