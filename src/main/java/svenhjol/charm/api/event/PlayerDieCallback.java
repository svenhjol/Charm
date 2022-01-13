package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public interface PlayerDieCallback {
    Event<PlayerDieCallback> EVENT = EventFactory.createArrayBacked(PlayerDieCallback.class, (listeners) -> (player, deathMessage) -> {
        for (PlayerDieCallback listener : listeners) {
            listener.interact(player, deathMessage);
        }
    });

    void interact(ServerPlayer player, Component deathMessage);
}
