package svenhjol.charm.feature.woodcutting;

import svenhjol.charm.feature.woodcutting.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(priority = 5, description = "Registers the woodcutting recipe.")
public final class Woodcutting extends CommonFeature {
    public final Registers registers;

    public Woodcutting(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }
}
