package svenhjol.charm.module.smooth_glowstone;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.loader.CharmModule;

public class SmoothGlowstoneBlock extends CharmBlock {
    public SmoothGlowstoneBlock(CharmModule module) {
        super(module, "smooth_glowstone", Properties.copy(Blocks.GLOWSTONE));
    }
}
