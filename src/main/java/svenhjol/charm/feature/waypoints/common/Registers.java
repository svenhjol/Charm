package svenhjol.charm.feature.waypoints.common;

import net.minecraft.sounds.SoundEvent;
import svenhjol.charm.charmony.event.PlayerLoginEvent;
import svenhjol.charm.charmony.event.PlayerTickEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.waypoints.Waypoints;
import svenhjol.charm.feature.waypoints.common.Networking.S2CClearWaypointInfo;
import svenhjol.charm.feature.waypoints.common.Networking.S2CUpdateWaypointInfo;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Waypoints> {
    public final Supplier<SoundEvent> broadcastSound;

    public Registers(Waypoints feature) {
        super(feature);
        var registry = feature.registry();

        broadcastSound = registry.soundEvent("waypoint_broadcast");

        // Server-to-client packet senders
        registry.serverPacketSender(S2CUpdateWaypointInfo.TYPE, S2CUpdateWaypointInfo.CODEC);
        registry.serverPacketSender(S2CClearWaypointInfo.TYPE, S2CClearWaypointInfo.CODEC);
    }

    @Override
    public void onEnabled() {
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
        PlayerLoginEvent.INSTANCE.handle(feature().handlers::playerLogin);
    }
}
