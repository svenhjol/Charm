package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class VariantBookshelfBlock extends Block implements IIgniteProvider {
    private final IVariantMaterial material;

    public VariantBookshelfBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.BOOKSHELF));
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

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantBookshelfBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
