package svenhjol.charm.module.variant_bookshelves;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.loader.CharmModule;

public class VariantBookshelfBlock extends CharmBlock {
    public VariantBookshelfBlock(CharmModule module, IWoodMaterial material) {
        super(module, material.getSerializedName() + "_bookshelf", Properties.copy(Blocks.BOOKSHELF));

        if (material.isFlammable()) {
            this.setFireInfo(30, 20);
        }

        if (material.isFlammable()) {
            this.setBurnTime(300);
        } else {
            this.setFireproof();
        }
    }
}
