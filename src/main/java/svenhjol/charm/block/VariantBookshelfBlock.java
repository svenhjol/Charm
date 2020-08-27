package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.IStorageMaterial;

public class VariantBookshelfBlock extends MesonBlock {
    public VariantBookshelfBlock(MesonModule module, IStorageMaterial type) {
        super(module, type.asString() + "_bookshelf", AbstractBlock.Settings.copy(Blocks.BOOKSHELF));

        /** @see net.minecraft.block.FireBlock */
        if (type.isFlammable())
            this.setFireInfo(30, 20);
    }

    @Override
    public int getBurnTime() {
        return 300;
    }

    // TODO doesn't seem possible to do enchantment power without several hooks
//    @Override
//    public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
//        return 1;
//    }
}
