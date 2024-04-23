package svenhjol.charm.foundation.server;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registry;

public class ServerFeature extends Feature {
    @Override
    public ServerLoader loader() {
        return (ServerLoader)loader;
    }

    @Override
    public Registry registry() {
        return null;
    }
}
