package svenhjol.charm.feature.item_restocking.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.item_restocking.ItemRestocking;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<ItemRestocking> {
    public Advancements(ItemRestocking feature) {
        super(feature);
    }

    public void restockedCurrentItem(Player player) {
        trigger("restocked_current_item", player);
    }
}
