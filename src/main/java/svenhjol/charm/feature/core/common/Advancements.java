package svenhjol.charm.feature.core.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Core> {
    public Advancements(Core feature) {
        super(feature);
    }

    public void playerJoined(Player player) {
        trigger("player_joined", player);
    }
}
