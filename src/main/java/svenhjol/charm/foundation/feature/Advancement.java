package svenhjol.charm.foundation.feature;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.common.Handlers;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Resolve;

public abstract class Advancement<F extends Feature> extends SetupRunner<F> {
    public void trigger(String id, Player player) {
        Resolve.support(Handlers.class).trigger(feature().id(id), player);
    }
}
