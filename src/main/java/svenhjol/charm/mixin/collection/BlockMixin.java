package svenhjol.charm.mixin.collection;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.collection.Collection;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
        method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private static void checkPosAndCancelDrops(Level level, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (Collection.trySpawnToInventory(level, pos, stack)) {
            ci.cancel();
        }
    }
    
    @Inject(
        method = "popResourceFromFace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private static void checkPosAndCancelDrops(Level level, BlockPos pos, Direction direction, ItemStack stack, CallbackInfo ci) {
        if (Collection.trySpawnToInventory(level, pos, stack)) {
            ci.cancel();
        }
    }
}