package svenhjol.charm.feature.parrots_stay_on_shoulder;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.parrots_stay_on_shoulder.common.Handlers;
import svenhjol.charm.feature.parrots_stay_on_shoulder.common.Registers;

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
