package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.feature.woodcutters.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.enums.Side;

@Feature(side = Side.COMMON, priority = 1,
    description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CommonFeature {
    public final Registers registers;

    public Woodcutters(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
