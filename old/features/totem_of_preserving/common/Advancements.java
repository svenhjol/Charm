package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.foundation.feature.Advancement;

public class Advancements extends Advancement<TotemOfPreserving> {
    public void usedTotem(Player player) {
        trigger("used_totem_of_preserving", player);
    }

    @Override
    protected Class<TotemOfPreserving> type() {
        return TotemOfPreserving.class;
    }
}
