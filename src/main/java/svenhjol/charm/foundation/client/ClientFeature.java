package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;

public class ClientFeature extends Feature {
    @Override
    public boolean canBeDisabled() {
        return false;
    }

    public Class<? extends CommonFeature> commonFeature() {
        return null;
    }
}
