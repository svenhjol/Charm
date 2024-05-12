package svenhjol.charm.foundation.feature;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.advancement.common.Handlers;

public abstract class AdvancementHolder<F extends Feature> extends FeatureHolder<F>{
    public AdvancementHolder(F feature) {
        super(feature);
    }

    protected void trigger(String id, Player player) {
        var res = feature().id(id);
        Handlers.trigger(res, player);
    }
}
