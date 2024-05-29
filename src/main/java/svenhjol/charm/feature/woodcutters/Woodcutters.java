package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.feature.woodcutters.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(priority = 1,
    description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public final class Woodcutters extends CommonFeature {
    public static final String BLOCK_ID = "woodcutter";

    public final Registers registers;

    public Woodcutters(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
