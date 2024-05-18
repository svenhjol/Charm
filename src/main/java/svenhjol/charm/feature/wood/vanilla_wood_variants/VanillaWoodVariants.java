package svenhjol.charm.feature.wood.vanilla_wood_variants;

import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.wood.vanilla_wood_variants.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Barrels, bookcases, chests and ladders in all vanilla wood types.")
public final class VanillaWoodVariants extends CommonFeature implements ChildFeature<Wood> {
    public final Registers registers;

    public VanillaWoodVariants(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<Wood> typeForParent() {
        return Wood.class;
    }
}
