package svenhjol.charm.feature.arcane_purpur.block;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.charmony.common.block.CharmBlock;
import svenhjol.charm.charmony.common.block.CharmBlockItem;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;

import java.util.function.Supplier;

public class ArcanePurpurBlock extends CharmBlock<ArcanePurpur> {
    public ArcanePurpurBlock() {
        super(Properties.copy(Blocks.PURPUR_BLOCK));
    }

    @Override
    public Class<ArcanePurpur> typeForFeature() {
        return ArcanePurpur.class;
    }

    public static class BlockItem extends CharmBlockItem<ArcanePurpurBlock> {
        public BlockItem(Supplier<ArcanePurpurBlock> block) {
            super(block, new Properties());
        }
    }
}
