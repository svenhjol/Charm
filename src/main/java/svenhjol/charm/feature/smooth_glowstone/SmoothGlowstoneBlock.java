package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class SmoothGlowstoneBlock extends CharmonyBlock {
    public SmoothGlowstoneBlock(CommonFeature feature) {
        super(feature, Properties.copy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public BlockItem(CommonFeature feature, Supplier<SmoothGlowstoneBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
