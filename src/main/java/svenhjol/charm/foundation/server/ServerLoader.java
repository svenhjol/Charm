package svenhjol.charm.foundation.server;

import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;

public class ServerLoader extends Loader<ServerFeature> {
    protected ServerLoader(String id) {
        super(id);
        this.log = new Log(id, "ServerLoader");
    }
}
