package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.iface.IFuelProvider;
import svenhjol.charmony.iface.IIgniteProvider;
import svenhjol.charmony_api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class VariantBookshelfBlock extends Block implements IIgniteProvider {
    private final IVariantMaterial material;

    public VariantBookshelfBlock(IVariantMaterial material) {
        super(Properties.copy(Blocks.BOOKSHELF));
        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    @Override
    public int igniteChance() {
        return material.igniteChance();
    }

    @Override
    public int burnChance() {
        return material.burnChance();
    }

    public static class BlockItem extends CharmonyBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantBookshelfBlock> block) {
            super(block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
