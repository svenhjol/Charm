package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charmapi.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;

import java.util.function.Supplier;

public class VariantLadderBlock extends LadderBlock {
    public VariantLadderBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.LADDER));
    }

    static VariantWood getParent() {
        return Charm.instance().loader().get(VariantWood.class).orElseThrow();
    }

    public static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(getParent(), block, new Properties());
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantLadders;
        }
    }
}
