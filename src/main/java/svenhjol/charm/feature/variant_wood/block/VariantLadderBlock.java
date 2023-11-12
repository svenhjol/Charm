package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.iface.IFuelProvider;
import svenhjol.charmony_api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class VariantLadderBlock extends LadderBlock {
    private final IVariantMaterial material;
    public VariantLadderBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.LADDER));
        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends CharmonyBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantLadderBlock> block) {
            super(block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
