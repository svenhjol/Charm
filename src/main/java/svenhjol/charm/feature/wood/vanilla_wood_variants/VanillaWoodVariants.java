package svenhjol.charm.feature.wood.vanilla_wood_variants;

import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.wood.vanilla_wood_variants.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature(priority = 1, description = "Barrels, bookcases, chests and ladders in all vanilla wood types.")
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
