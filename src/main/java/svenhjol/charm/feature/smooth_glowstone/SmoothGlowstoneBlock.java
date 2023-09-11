package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmBlock;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;

import java.util.function.Supplier;

public class SmoothGlowstoneBlock extends CharmBlock {
    public SmoothGlowstoneBlock(CharmFeature feature) {
        super(feature, Properties.copy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends CharmBlockItem {
        public BlockItem(CharmFeature feature, Supplier<SmoothGlowstoneBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
