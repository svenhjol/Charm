package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;

public class ClientFeature extends Feature {
    @Override
    public ClientLoader loader() {
        return (ClientLoader)loader;
    }

    @Override
    public ClientRegistry registry() {
        return loader().registry();
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    public Class<? extends CommonFeature> commonFeature() {
        return null;
    }
}
