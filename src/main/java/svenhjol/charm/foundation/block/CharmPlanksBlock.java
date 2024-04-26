package svenhjol.charm.foundation.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.api.iface.IIgniteProvider;
import svenhjol.charm.api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class CharmPlanksBlock extends Block implements IIgniteProvider {
    protected final IVariantMaterial variantMaterial;

    public CharmPlanksBlock(IVariantMaterial material) {
        super(material.blockProperties()
            .strength(2.0F, 3.0F));

        this.variantMaterial = material;
    }

    @Override
    public int igniteChance() {
        return variantMaterial.isFlammable() ? 5 : 0;
    }

    @Override
    public int burnChance() {
        return variantMaterial.isFlammable() ? 20 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<CharmPlanksBlock> block) {
            super(block.get(), new Item.Properties());
        }
    }
}
