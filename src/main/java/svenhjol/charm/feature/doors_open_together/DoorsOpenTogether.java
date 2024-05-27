package svenhjol.charm.feature.doors_open_together;

import svenhjol.charm.feature.doors_open_together.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Automatically opens double doors.")
public final class DoorsOpenTogether extends CommonFeature {
    public final Handlers handlers;

    public DoorsOpenTogether(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }
}
