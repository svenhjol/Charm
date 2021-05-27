package svenhjol.charm.module.more_portal_frames;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.init.CharmTags;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Crying obsidian and refined obsidian can be used to construct nether portal frames.",
    requiresMixins = {"more_portal_frames.*"})
public class MorePortalFrames extends CharmModule {
    public static boolean isValidBlock(BlockState blockState) {
        if (!ModuleHandler.enabled(MorePortalFrames.class))
            return blockState.isOf(Blocks.OBSIDIAN); // vanilla

        return blockState.isIn(CharmTags.NETHER_PORTAL_FRAMES);
    }
}
