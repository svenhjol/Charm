package svenhjol.charm.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.module.MorePortalFrames;

@Mixin(AreaHelper.class)
public class AreaHelperMixin {
    @Final @Shadow private static final AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK = (blockState, blockView, blockPos) -> {
        if (!ModuleHandler.enabled(MorePortalFrames.class))
            return blockState.isOf(Blocks.OBSIDIAN); // vanilla

        return MorePortalFrames.isValidBlock(blockState);
    };
}
