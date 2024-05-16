package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmBookshelfBlock;

import java.util.function.Supplier;

public class CustomBookshelf {
    public final Supplier<CharmBookshelfBlock> block;
    public final Supplier<CharmBookshelfBlock.BlockItem> item;

    public CustomBookshelf(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_bookshelf";

        block = registry.block(id, () -> new CharmBookshelfBlock(material));
        item = registry.item(id, () -> new CharmBookshelfBlock.BlockItem(block));

        // Bookshelves can set on fire.
        registry.ignite(block);

        // Bookshelves can be used as furnace fuel.
        registry.fuel(item);

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.BOOKSHELF, item);
    }
}
