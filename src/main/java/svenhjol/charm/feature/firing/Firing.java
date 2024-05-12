package svenhjol.charm.feature.firing;

import svenhjol.charm.feature.firing.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(priority = 5, description = "Registers the firing recipe.")
public class Firing extends CommonFeature {
    public final Registers registers;

    public Firing(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }
}
