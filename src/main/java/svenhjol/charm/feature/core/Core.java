package svenhjol.charm.feature.core;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class Core extends CommonFeature {
    public static final String PLAYER_JOINED = "player_joined";

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    void handlePlayerLogin(Player player) {
        Advancements.trigger(Charm.id(PLAYER_JOINED), player);
    }
}
