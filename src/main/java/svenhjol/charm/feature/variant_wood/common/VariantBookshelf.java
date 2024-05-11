package svenhjol.charm.feature.variant_wood.common;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.BookshelfBlock;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.function.Supplier;

public class VariantBookshelf implements FeatureResolver<VariantWood> {
    public final Supplier<BookshelfBlock> block;
    public final Supplier<BookshelfBlock.BlockItem> item;

    public VariantBookshelf(CommonFeature owner, IVariantMaterial material) {
        var registry = owner.registry();
        var id = material.getSerializedName() + "_bookshelf";

        block = registry.block(id, () -> new BookshelfBlock(material));
        item = registry.item(id, () -> new BookshelfBlock.BlockItem(block));

        // Bookshelves can set on fire.
        registry.ignite(block);

        // Bookshelves can be used as furnace fuel.
        registry.fuel(item);
    }

    @Override
    public Class<VariantWood> typeForFeature() {
        return VariantWood.class;
    }
}
