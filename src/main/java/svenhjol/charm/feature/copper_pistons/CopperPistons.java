package svenhjol.charm.feature.copper_pistons;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.copper_pistons.common.Registers;

@Feature(description = "Copper Pistons do not have quasi-connectivity.")
public final class CopperPistons extends CommonFeature {
    public final Registers registers;

    public CopperPistons(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
