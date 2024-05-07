package svenhjol.charm.foundation.feature;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.common.Handlers;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

public abstract class Advancement<T extends Feature> implements SetupRunner {
    private T resolved;

    public T feature() {
        if (resolved == null) {
            resolved = Resolve.feature(type());
        }
        return resolved;
    }

    protected abstract Class<T> type();

    public Log log() {
        return feature().log();
    }

    public void trigger(String id, Player player) {
        Resolve.support(Handlers.class).trigger(feature().id(id), player);
    }
}
