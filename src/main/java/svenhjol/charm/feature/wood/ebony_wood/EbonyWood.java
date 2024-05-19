package svenhjol.charm.feature.wood.ebony_wood;

import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.wood.ebony_wood.common.Providers;
import svenhjol.charm.feature.wood.ebony_wood.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(description = "Ebony is a dark wood obtainable from ebony trees that grow in savanna biomes.")
public final class EbonyWood extends CommonFeature implements ChildFeature<Wood> {
    public static final String BOAT_ID = "charm_ebony";
    public final Registers registers;
    public final Providers providers;

    public EbonyWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }

    @Override
    public Class<Wood> typeForParent() {
        return Wood.class;
    }
}
