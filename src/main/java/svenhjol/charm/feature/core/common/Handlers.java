package svenhjol.charm.feature.core.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.Core;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<Core> {
    public Handlers(Core feature) {
        super(feature);
    }

    public void playerLogin(Player player) {
        feature().advancements.playerJoined(player);
    }
}
