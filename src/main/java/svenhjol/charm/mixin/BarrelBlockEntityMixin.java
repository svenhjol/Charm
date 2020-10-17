package svenhjol.charm.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.base.CharmTags;

@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin {
    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
        )
    )
    private boolean hookTickCheckBlockState(BlockState blockState, Block block) {
        return blockState.isIn(CharmTags.BARRELS);
    }
}
