package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.feature.azalea_wood.common.Handlers;
import svenhjol.charm.feature.azalea_wood.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CommonFeature {
    public static final String BOAT_ID = "charm_azalea";
    public final Registers registers;
    public final Handlers handlers;

    public AzaleaWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
