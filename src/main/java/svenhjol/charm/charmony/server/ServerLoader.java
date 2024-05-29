package svenhjol.charm.charmony.server;

import svenhjol.charm.charmony.Loader;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.enums.Side;

public class ServerLoader extends Loader<ServerFeature> {
    protected ServerLoader(String id) {
        super(id);
        this.log = new Log(id, this);
    }

    @Override
    public Side side() {
        return Side.SERVER;
    }

    @Override
    protected Class<? extends Loader<ServerFeature>> type() {
        return ServerLoader.class;
    }
}
