package svenhjol.charm.feature.waypoints.client;

import svenhjol.charm.charmony.event.HudRenderEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.waypoints.WaypointsClient;
import svenhjol.charm.feature.waypoints.common.Networking;

public final class Registers extends RegisterHolder<WaypointsClient> {
    public Registers(WaypointsClient feature) {
        super(feature);
        var registry = feature.registry();

        registry.clientPacketReceiver(new Networking.S2CUpdateWaypointInfo(),
            () -> feature.handlers::updateWaypointInfoReceived);
        registry.clientPacketReceiver(new Networking.S2CClearWaypointInfo(),
            () -> feature.handlers::clearWaypointInfoReceived);
    }

    @Override
    public void onEnabled() {
        HudRenderEvent.INSTANCE.handle(feature().handlers::hudRender);
    }
}
