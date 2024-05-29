package svenhjol.charm.charmony.common;

import svenhjol.charm.charmony.Feature;

public class CommonFeature extends Feature {
    public CommonFeature(CommonLoader loader) {
        super(loader);
    }

    @Override
    public CommonLoader loader() {
        return (CommonLoader)super.loader();
    }

    @Override
    public CommonRegistry registry() {
        return loader().registry();
    }
}
