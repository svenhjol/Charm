package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Feature;

public class ClientFeature extends Feature {
    public ClientFeature(ClientLoader loader) {
        super(loader);
    }

    @Override
    public ClientLoader loader() {
        return (ClientLoader)loader;
    }

    @Override
    public ClientRegistry registry() {
        return loader().registry();
    }
}
