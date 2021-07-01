package svenhjol.charm.module.more_portal_frames;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.init.CharmTags;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Crying obsidian can be used to construct nether portal frames.")
public class MorePortalFrames extends CharmCommonModule {
    public static boolean isValidBlock(BlockState blockState) {
        if (!Charm.LOADER.isEnabled(MorePortalFrames.class))
            return blockState.is(Blocks.OBSIDIAN); // vanilla

        return blockState.is(CharmTags.NETHER_PORTAL_FRAMES);
    }
}
