package svenhjol.charm.feature.variant_wood.registry;

import svenhjol.charm.feature.variant_wood.block.VariantLadderBlock;
import svenhjol.charmapi.iface.IVariantMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.function.Supplier;

public class CustomLadder {
    public final Supplier<VariantLadderBlock> block;
    public final Supplier<VariantLadderBlock.BlockItem> item;

    public CustomLadder(ICommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_ladder";

        block = registry.block(id, () -> new VariantLadderBlock(material));
        item = registry.item(id, () -> new VariantLadderBlock.BlockItem(block));
    }
}
