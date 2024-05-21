package svenhjol.charm.feature.parrots_stay_on_shoulder;

import svenhjol.charm.feature.parrots_stay_on_shoulder.common.Handlers;
import svenhjol.charm.feature.parrots_stay_on_shoulder.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Parrots stay on player's shoulder when jumping and falling. Crouch to make them dismount.")
public final class ParrotsStayOnShoulder extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public ParrotsStayOnShoulder(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
