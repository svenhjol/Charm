package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

public class SmoothGlowstoneBlock extends MesonBlock {
    public SmoothGlowstoneBlock(MesonModule module) {
        super(module, "smooth_glowstone", Settings.copy(Blocks.GLOWSTONE));
    }
}
