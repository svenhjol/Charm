package svenhjol.charm.charmony.client;

import svenhjol.charm.charmony.Feature;

public class ClientFeature extends Feature {
    public ClientFeature(ClientLoader loader) {
        super(loader);
    }

    @Override
    public ClientLoader loader() {
        return (ClientLoader)super.loader();
    }

    @Override
    public ClientRegistry registry() {
        return loader().registry();
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }
}
