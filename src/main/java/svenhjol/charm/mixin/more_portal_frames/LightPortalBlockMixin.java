package svenhjol.charm.mixin.more_portal_frames;

import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.more_portal_frames.MorePortalFrames;

@Mixin(BaseFireBlock.class)
public class LightPortalBlockMixin {
    @Redirect(
        method = "isPortal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private static boolean hookIsPortal(BlockState state, Block block) {
        return MorePortalFrames.isValidBlock(state);
    }
}
