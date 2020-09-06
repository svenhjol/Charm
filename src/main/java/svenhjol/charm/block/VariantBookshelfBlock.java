package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.IVariantMaterial;

public class VariantBookshelfBlock extends MesonBlock {
    public VariantBookshelfBlock(MesonModule module, IVariantMaterial type) {
        super(module, type.asString() + "_bookshelf", AbstractBlock.Settings.copy(Blocks.BOOKSHELF));

        /** @see net.minecraft.block.FireBlock */
        if (type.isFlammable())
            this.setFireInfo(30, 20);

        this.setBurnTime(300);
    }
}
