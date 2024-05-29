package svenhjol.charm.charmony.server;

import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Registry;

public class ServerFeature extends Feature {
    public ServerFeature(ServerLoader loader) {
        super(loader);
    }

    @Override
    public ServerLoader loader() {
        return (ServerLoader)super.loader();
    }

    @Override
    public Registry registry() {
        return null;
    }
}
