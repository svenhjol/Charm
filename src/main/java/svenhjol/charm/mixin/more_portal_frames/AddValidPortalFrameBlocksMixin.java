package svenhjol.charm.mixin.more_portal_frames;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import svenhjol.charm.module.more_portal_frames.MorePortalFrames;

@Mixin(PortalShape.class)
public class AddValidPortalFrameBlocksMixin {
    /**
     * Override the default valid frame block predicate with the one provided by MorePortalFrames module.
     */
    @Final
    @Shadow private static final BlockBehaviour.StatePredicate IS_VALID_FRAME_BLOCK = (blockState, blockView, blockPos) -> MorePortalFrames.isValidBlock(blockState);
}
