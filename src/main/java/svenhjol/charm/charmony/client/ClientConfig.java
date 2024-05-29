package svenhjol.charm.charmony.client;

import svenhjol.charm.charmony.Config;

public class ClientConfig extends Config {
    public ClientConfig(ClientLoader loader) {
        super(loader.id() + "-client");
    }
}
