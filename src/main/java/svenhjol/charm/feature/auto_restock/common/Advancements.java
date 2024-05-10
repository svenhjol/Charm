package svenhjol.charm.feature.auto_restock.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.auto_restock.AutoRestock;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<AutoRestock> {
    public Advancements(AutoRestock feature) {
        super(feature);
    }

    public void restockedCurrentItem(Player player) {
        trigger("restocked_current_item", player);
    }
}
