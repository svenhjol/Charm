package svenhjol.charm.mixin.collection_enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.collection_enchantment.CollectionEnchantment;

@Mixin(Block.class)
public class AddDroppedItemToInventoryMixin {
    /**
     * Default vanilla behavior is to spawn the itementity in the world.
     * This method defers to the Collection enchantment to add the item
     * to the player's inventory instead, in which case the vanilla
     * method is cancelled (the itementity does not spawn).
     */
    @Inject(method = "dropStack", at = @At("HEAD"), cancellable = true)
    private static void hookDropStack(Level world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (CollectionEnchantment.trySpawnToInventory(world, stack))
            ci.cancel();
    }
}
