package svenhjol.charm.feature.arcane_purpur;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class ArcanePurpurBlock extends CharmonyBlock {
    public ArcanePurpurBlock(CommonFeature feature) {
        super(feature, Properties.copy(Blocks.PURPUR_BLOCK));
    }

    public static class BlockItem extends CharmonyBlockItem {
        public BlockItem(CommonFeature feature, Supplier<ArcanePurpurBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
