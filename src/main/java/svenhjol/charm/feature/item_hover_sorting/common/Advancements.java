package svenhjol.charm.feature.item_hover_sorting.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ItemHoverSorting> {
    public Advancements(ItemHoverSorting feature) {
        super(feature);
    }

    public void hoverSortedItems(Player player) {
        trigger("hover_sorted_items", player);
    }
}
