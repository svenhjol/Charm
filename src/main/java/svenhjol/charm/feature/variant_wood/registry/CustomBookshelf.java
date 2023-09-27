package svenhjol.charm.feature.variant_wood.registry;

import svenhjol.charm.feature.variant_wood.block.VariantBookshelfBlock;
import svenhjol.charmony_api.iface.IVariantMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.function.Supplier;

public class CustomBookshelf {
    public final Supplier<VariantBookshelfBlock> block;
    public final Supplier<VariantBookshelfBlock.BlockItem> item;

    public CustomBookshelf(ICommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_bookshelf";

        block = registry.block(id, () -> new VariantBookshelfBlock(material));
        item = registry.item(id, () -> new VariantBookshelfBlock.BlockItem(block));

        // Bookshelves can set on fire.
        registry.ignite(block);

        // Bookshelves can be used as furnace fuel.
        registry.fuel(item);
    }
}
