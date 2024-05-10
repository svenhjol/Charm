package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charm.feature.vanilla_wood_variants.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Barrels, bookcases, chests and ladders in all vanilla wood types.")
public class VanillaWoodVariants extends CommonFeature {
    public final Registers registers;

    public VanillaWoodVariants(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
