package svenhjol.charm.charmony.server;

import svenhjol.charm.charmony.Loader;
import svenhjol.charm.charmony.Log;

public class ServerLoader extends Loader<ServerFeature> {
    protected ServerLoader(String id) {
        super(id);
        this.log = new Log(id, this);
    }

    @Override
    protected Class<? extends Loader<ServerFeature>> type() {
        return ServerLoader.class;
    }
}
