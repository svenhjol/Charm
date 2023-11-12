package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;

import java.util.function.Supplier;

public class SmoothGlowstoneBlock extends CharmonyBlock {
    public SmoothGlowstoneBlock() {
        super(Properties.copy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public BlockItem(Supplier<SmoothGlowstoneBlock> block) {
            super(block, new Properties());
        }
    }
}
