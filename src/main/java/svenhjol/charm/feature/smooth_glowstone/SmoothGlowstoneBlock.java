package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

public class SmoothGlowstoneBlock extends CharmonyBlock {
    public SmoothGlowstoneBlock(CharmonyFeature feature) {
        super(feature, Properties.copy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public BlockItem(CharmonyFeature feature, Supplier<SmoothGlowstoneBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
