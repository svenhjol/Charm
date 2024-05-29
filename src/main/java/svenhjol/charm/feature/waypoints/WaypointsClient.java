package svenhjol.charm.feature.waypoints;

import svenhjol.charm.feature.waypoints.client.Handlers;
import svenhjol.charm.feature.waypoints.client.Providers;
import svenhjol.charm.feature.waypoints.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;

@Feature
public final class WaypointsClient extends ClientFeature implements CommonResolver<Waypoints> {
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
    public Class<Waypoints> typeForCommon() {
        return Waypoints.class;
    }
}
