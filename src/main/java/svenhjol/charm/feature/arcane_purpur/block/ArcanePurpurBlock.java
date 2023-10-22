package svenhjol.charm.feature.arcane_purpur.block;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class ArcanePurpurBlock extends CharmonyBlock {
    public ArcanePurpurBlock(CommonFeature feature) {
        super(feature, Properties.copy(Blocks.PURPUR_BLOCK));
    }

    public static class BlockItem<T extends ArcanePurpurBlock> extends CharmonyBlockItem {
        public BlockItem(CommonFeature feature, Supplier<T> block) {
            super(feature, block, new Properties());
        }
    }
}
