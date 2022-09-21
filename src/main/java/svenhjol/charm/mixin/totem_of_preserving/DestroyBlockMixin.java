package svenhjol.charm.mixin.totem_of_preserving;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.totem_of_preserving.TotemOfPreserving;

@Mixin(Level.class)
public abstract class DestroyBlockMixin {
    @Shadow public abstract ResourceKey<Level> dimension();
    
    @Inject(
        method = "destroyBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookDestroyBlock(BlockPos pos, boolean bl, Entity entity, int i, CallbackInfoReturnable<Boolean> cir) {
        if (isProtected(pos)) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(
        method = "removeBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRemoveBlock(BlockPos pos, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (isProtected(pos)) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(
        method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSetBlock(BlockPos pos, BlockState blockState, int i, int j, CallbackInfoReturnable<Boolean> cir) {
        if (isProtected(pos)) {
            cir.setReturnValue(false);
        }
    }
    
    private boolean isProtected(BlockPos pos) {
        var dimension = this.dimension().location();
        return TotemOfPreserving.PROTECT_POSITIONS.containsKey(dimension)
            && TotemOfPreserving.PROTECT_POSITIONS.get(dimension).contains(pos);
    }
}