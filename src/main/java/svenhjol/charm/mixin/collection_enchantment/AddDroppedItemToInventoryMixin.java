package svenhjol.charm.mixin.collection_enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.collection_enchantment.CollectionEnchantment;

/**
 * Inject a hook before calls to popResource.
 * The hook tests the block pos that is currently being mined, and if
 * that block position is associated with a player, the dropped stack
 * is automatically given to the player and the calls to popResource
 * are cancelled so the stack never gets spawned in the world.
 */
@Mixin(Block.class)
public class AddDroppedItemToInventoryMixin {
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
        if (CollectionEnchantment.trySpawnToInventory(level, pos, stack)) {
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
        if (CollectionEnchantment.trySpawnToInventory(level, pos, stack)) {
            ci.cancel();
        }
    }
}
