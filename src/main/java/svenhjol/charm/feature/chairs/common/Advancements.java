package svenhjol.charm.feature.chairs.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.chairs.Chairs;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<Chairs> {
    public Advancements(Chairs feature) {
        super(feature);
    }

    public void satOnChair(Player player) {
        trigger("sat_on_chair", player);
    }
}
