package svenhjol.charm.api.iface;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * Specify a list of items, block tags or item tags that have contents
 * that may be sorted using Charm's HoverSorting feature.
 */
@SuppressWarnings("unused")
public interface IHoverSortableItemProvider {
    /**
     * List of items that allow hover sorting.
     */
    List<ItemLike> getHoverSortableItems();

    /**
     * List of BlockTags of which all blocks will allow hover sorting.
     * @return List of BlockTags.
     */
    default List<TagKey<Block>> getHoverSortableBlockTags() {
        return List.of();
    }

    /**

     * List of ItemTags of which all items will allow hover sorting.
     * @return List of ItemTags.
     */
    default List<TagKey<Item>> getHoverSortableItemTags() {
        return List.of();
    }
}
