package svenhjol.charm.feature.variant_ladders;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;

import java.util.function.Supplier;

public class VariantLadderBlock extends LadderBlock {
    private final CharmFeature feature;

    public VariantLadderBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.LADDER));
        this.feature = getParent();
    }

    private static VariantLadders getParent() {
        return Charm.instance().loader().get(VariantLadders.class).orElseThrow();
    }

    public static class BlockItem extends CharmBlockItem {
        public <T extends Block> BlockItem(Supplier<T> block) {
            super(getParent(), block, new Properties());
        }
    }
}
