package svenhjol.charm.feature.chairs.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.foundation.feature.Advancement;

public final class Advancements extends Advancement<Chairs> {
    @Override
    protected Class<Chairs> type() {
        return Chairs.class;
    }

    public void satOnChair(Player player) {
        trigger("sat_on_chair", player);
    }
}
