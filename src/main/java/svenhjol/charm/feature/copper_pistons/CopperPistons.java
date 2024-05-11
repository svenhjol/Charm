package svenhjol.charm.feature.copper_pistons;

import svenhjol.charm.feature.copper_pistons.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Copper Pistons do not have quasi-connectivity.")
public class CopperPistons extends CommonFeature {
    public final Registers registers;

    public CopperPistons(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
