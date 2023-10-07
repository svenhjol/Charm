package svenhjol.charm.feature.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony_api.event.PlayerLoginEvent;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false)
public class Core extends CharmonyFeature {
    static final ResourceLocation TRIGGER_PLAYER_JOINED = Charm.instance().makeId("player_joined");

    @Override
    public void runWhenEnabled() {
        PlayerLoginEvent.INSTANCE.handle(this::handlePlayerLogin);
    }

    private void handlePlayerLogin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Advancements.trigger(TRIGGER_PLAYER_JOINED, serverPlayer);
        }
    }
}
