package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public interface PlayerDieCallback {
    Event<PlayerDieCallback> EVENT = EventFactory.createArrayBacked(PlayerDieCallback.class, (listeners) -> (player, source) -> {
        for (PlayerDieCallback listener : listeners) {
            listener.interact(player, source);
        }
    });

    void interact(ServerPlayer player, DamageSource source);
}
