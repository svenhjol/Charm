package svenhjol.charm.feature.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.PlayerLoginEvent;

public class Core extends CommonFeature {
    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void runWhenEnabled() {
        PlayerLoginEvent.INSTANCE.handle(this::handlePlayerLogin);
    }

    private void handlePlayerLogin(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "player_joined"), player);
    }
}
