package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomBookshelfBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class BookshelfHolder {
    public final Supplier<CustomBookshelfBlock> block;
    public final Supplier<CustomBookshelfBlock.BlockItem> item;

    public BookshelfHolder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_bookshelf";

        block = registry.block(id, () -> new CustomBookshelfBlock(material));
        item = registry.item(id, () -> new CustomBookshelfBlock.BlockItem(block));

        // Bookshelves can set on fire.
        registry.ignite(block);

        // Bookshelves can be used as furnace fuel.
        registry.fuel(item);

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.BOOKSHELF, item);
    }
}
