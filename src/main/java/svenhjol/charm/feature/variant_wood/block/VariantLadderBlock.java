package svenhjol.charm.feature.variant_wood.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.Mods;
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

    static VariantWood getParent() {
        return Mods.common(Charm.ID).loader().get(VariantWood.class).orElseThrow();
    }

    public static class BlockItem extends CharmonyBlockItem implements IFuelProvider {
        private final IVariantMaterial material;

        public BlockItem(Supplier<VariantLadderBlock> block) {
            super(getParent(), block, new Properties());
            this.material = block.get().getMaterial();
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && VariantWood.variantLadders;
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
