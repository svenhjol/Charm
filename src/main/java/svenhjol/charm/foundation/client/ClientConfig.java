package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Config;

public class ClientConfig extends Config {
    public ClientConfig(ClientLoader loader) {
        super(loader.id() + "-client");
    }
}
