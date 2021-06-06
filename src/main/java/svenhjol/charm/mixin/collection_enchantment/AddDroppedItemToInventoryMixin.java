package svenhjol.charm.mixin.collection_enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
     *
     * TODO: when this was mapped from yarn there were three potential candidate methods - check works as expected
     */
    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void hookDropStack(BlockState blockState, Level level, BlockPos blockPos, BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfo ci) {
        if (trySpawnToInventory(level, itemStack))
            ci.cancel();
    }

    private static boolean trySpawnToInventory(Level world, ItemStack stack) {
        return CollectionEnchantment.trySpawnToInventory(world, stack);
    }
}
