package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.function.Supplier;

public class CustomBookshelf {
    public final Supplier<VariantBookshelfBlock> block;
    public final Supplier<VariantBookshelfBlock.BlockItem> item;

    public CustomBookshelf(CommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_bookshelf";

        block = registry.block(id, () -> new VariantBookshelfBlock(material));
        item = registry.item(id, () -> new VariantBookshelfBlock.BlockItem(block));

        // Bookshelves can set on fire.
        registry.ignite(block);

        // Bookshelves can be used as furnace fuel.
        registry.fuel(item);
    }
}
