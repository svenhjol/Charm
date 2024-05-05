package svenhjol.charm.foundation.feature;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;

public abstract class Advancement<T extends Feature> {
    protected T feature;

    public Advancement(T feature) {
        this.feature = feature;
        log().debug("Initializing advancement class " + name() + " for " + feature.name());
    }

    public Log log() {
        return feature.log();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    public void trigger(String id, Player player) {
        Advancements.trigger(feature.id(id), player);
    }
}
