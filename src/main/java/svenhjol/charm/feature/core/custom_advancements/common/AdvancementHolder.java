package svenhjol.charm.feature.core.custom_advancements.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.feature.FeatureHolder;

public abstract class AdvancementHolder<F extends Feature> extends FeatureHolder<F> {
    public AdvancementHolder(F feature) {
        super(feature);
    }

    protected void trigger(String id, Player player) {
        var res = feature().id(id);
        Resolve.feature(CustomAdvancements.class).handlers.trigger(res, player);
    }
}
