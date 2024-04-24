package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.function.Supplier;

public class CustomLadder {
    public final Supplier<VariantLadderBlock> block;
    public final Supplier<VariantLadderBlock.BlockItem> item;

    public CustomLadder(CommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_ladder";

        block = registry.block(id, () -> new VariantLadderBlock(material));
        item = registry.item(id, () -> new VariantLadderBlock.BlockItem(block));

        // Ladders can be used as furnace fuel.
        registry.fuel(item);
    }
}
