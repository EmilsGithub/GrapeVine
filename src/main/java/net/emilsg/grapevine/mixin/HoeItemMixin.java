package net.emilsg.grapevine.mixin;

import net.emilsg.grapevine.block.crop.TrellisFrameBlock;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(method = "canTillFarmland", at = @At(value = "RETURN"), cancellable = true)
    private static void canTillFarmlandWithTrellis(ItemUsageContext context, CallbackInfoReturnable<Boolean> cir) {
        if(context.getSide() != Direction.DOWN && context.getWorld().getBlockState(context.getBlockPos().up()).getBlock() instanceof TrellisFrameBlock) cir.setReturnValue(true);
    }

}
