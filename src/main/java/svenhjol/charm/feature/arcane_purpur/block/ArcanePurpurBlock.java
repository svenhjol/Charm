package svenhjol.charm.feature.arcane_purpur.block;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlock;
import svenhjol.charmony.base.CharmonyBlockItem;

import java.util.function.Supplier;

public class ArcanePurpurBlock extends CharmonyBlock {
    public ArcanePurpurBlock() {
        super(Properties.copy(Blocks.PURPUR_BLOCK));
    }

    public static class BlockItem<T extends ArcanePurpurBlock> extends CharmonyBlockItem {
        public BlockItem(Supplier<T> block) {
            super(block, new Properties());
        }
    }
}
