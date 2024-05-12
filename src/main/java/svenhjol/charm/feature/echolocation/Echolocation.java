package svenhjol.charm.feature.echolocation;

import svenhjol.charm.feature.echolocation.common.Handlers;
import svenhjol.charm.feature.echolocation.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(priority = 1, description = "A status effect that causes all living entities around the player to glow.")
public final class Echolocation extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public Echolocation(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false; // Other features depend on this base feature.
    }
}
