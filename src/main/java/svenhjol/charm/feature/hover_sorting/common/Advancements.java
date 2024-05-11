package svenhjol.charm.feature.hover_sorting.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<HoverSorting> {
    public Advancements(HoverSorting feature) {
        super(feature);
    }

    public void triggerSortedItems(Player player) {
        trigger("sorted_items", player);
    }
}
