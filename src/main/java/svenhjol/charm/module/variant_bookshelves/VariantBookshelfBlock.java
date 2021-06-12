package svenhjol.charm.module.variant_bookshelves;

import svenhjol.charm.module.CharmModule;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.enums.IVariantMaterial;

public class VariantBookshelfBlock extends CharmBlock {
    public VariantBookshelfBlock(CharmModule module, IVariantMaterial type, String... loadedMods) {
        super(module, type.getSerializedName() + "_bookshelf", BlockBehaviour.Properties.copy(Blocks.BOOKSHELF), loadedMods);

        if (type.isFlammable())
            this.setFireInfo(30, 20);

        this.setBurnTime(300);
    }
}
