package svenhjol.charm.mixin.collection_enchantment;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.CollectionEnchantment;

@Mixin(Block.class)
public class AddDroppedItemToInventoryMixin {
    /**
     * Default vanilla behavior is to spawn the itementity in the world.
     * This method defers to the Collection enchantment to add the item
     * to the player's inventory instead, in which case the vanilla
     * method is cancelled (the itementity does not spawn).
     */
    @Inject(method = "dropStack", at = @At("HEAD"), cancellable = true)
    private static void hookDropStack(World world, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        if (CollectionEnchantment.trySpawnToInventory(world, stack))
            ci.cancel();
    }
}
