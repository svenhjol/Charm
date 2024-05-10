package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.api.iface.IFuelProvider;
import svenhjol.charm.api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class LadderBlock extends net.minecraft.world.level.block.LadderBlock {
    private final IVariantMaterial material;
    public LadderBlock(IVariantMaterial material) {
        super(Properties.ofFullCopy(Blocks.LADDER));
        this.material = material;
    }

    public IVariantMaterial getMaterial() {
        return material;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<LadderBlock> block) {
            super(block.get(), new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
