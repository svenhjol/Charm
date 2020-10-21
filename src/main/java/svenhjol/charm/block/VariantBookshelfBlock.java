package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;
import svenhjol.charm.base.enums.IVariantMaterial;

public class VariantBookshelfBlock extends CharmBlock {
    public VariantBookshelfBlock(CharmModule module, IVariantMaterial type) {
        super(module, type.asString() + "_bookshelf", AbstractBlock.Settings.copy(Blocks.BOOKSHELF));

        /** @see net.minecraft.block.FireBlock */
        if (type.isFlammable())
            this.setFireInfo(30, 20);

        this.setBurnTime(300);
    }
}
