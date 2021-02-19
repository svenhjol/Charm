package svenhjol.charm.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.Acquisition;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "dropStack", at = @At("HEAD"), cancellable = true)
    private static void hookSpawnAsEntity(World worldIn, BlockPos pos, ItemStack stack, CallbackInfo info) {
        if (Acquisition.trySpawnToInventory(worldIn, stack)) {
            info.cancel();
        }
    }
}
