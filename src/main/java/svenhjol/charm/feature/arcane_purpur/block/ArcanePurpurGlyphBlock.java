package svenhjol.charm.feature.arcane_purpur.block;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.charmony.common.block.CharmBlock;
import svenhjol.charm.charmony.common.block.CharmBlockItem;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;

import java.util.function.Supplier;

public class ArcanePurpurGlyphBlock extends CharmBlock<ArcanePurpur> {
    public ArcanePurpurGlyphBlock() {
        super(Properties.copy(Blocks.PURPUR_BLOCK));
    }

    @Override
    public Class<ArcanePurpur> typeForFeature() {
        return ArcanePurpur.class;
    }

    public static class BlockItem extends CharmBlockItem<ArcanePurpurGlyphBlock> {
        public BlockItem(Supplier<ArcanePurpurGlyphBlock> block) {
            super(block, new Properties());
        }
    }
}
