package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Config;
import svenhjol.charm.foundation.Log;

public class ClientConfig extends Config {
    public ClientConfig(String id, Log log) {
        super(id + "-client", log);
    }
}
