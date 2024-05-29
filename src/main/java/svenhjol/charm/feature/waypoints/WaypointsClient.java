package svenhjol.charm.feature.waypoints;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.waypoints.client.Handlers;
import svenhjol.charm.feature.waypoints.client.Providers;
import svenhjol.charm.feature.waypoints.client.Registers;

@Feature
public final class WaypointsClient extends ClientFeature implements LinkedFeature<Waypoints> {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    public WaypointsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    @Override
    public Class<Waypoints> typeForLinked() {
        return Waypoints.class;
    }
}
