package svenhjol.charm.feature.wood.azalea_wood;

import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm.feature.wood.azalea_wood.common.Providers;
import svenhjol.charm.feature.wood.azalea_wood.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature(description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public final class AzaleaWood extends CommonFeature implements ChildFeature<Wood> {
    public static final String BOAT_ID = "charm_azalea";
    public final Registers registers;
    public final Providers providers;

    public AzaleaWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }

    @Override
    public Class<Wood> typeForParent() {
        return Wood.class;
    }
}
