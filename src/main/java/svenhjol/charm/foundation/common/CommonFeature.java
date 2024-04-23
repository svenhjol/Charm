package svenhjol.charm.foundation.common;

import svenhjol.charm.foundation.Feature;

public class CommonFeature extends Feature {
    @Override
    public CommonLoader loader() {
        return (CommonLoader)loader;
    }

    @Override
    public CommonRegistry registry() {
        return loader().registry();
    }
}
