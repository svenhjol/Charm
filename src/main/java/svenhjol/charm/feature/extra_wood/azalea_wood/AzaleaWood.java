package svenhjol.charm.feature.extra_wood.azalea_wood;

import svenhjol.charm.feature.extra_wood.ExtraWood;
import svenhjol.charm.feature.extra_wood.azalea_wood.common.Handlers;
import svenhjol.charm.feature.extra_wood.azalea_wood.common.Providers;
import svenhjol.charm.feature.extra_wood.azalea_wood.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public final class AzaleaWood extends CommonFeature implements SubFeature<ExtraWood> {
    public static final String BOAT_ID = "charm_azalea";
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    public AzaleaWood(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    @Override
    public Class<ExtraWood> typeForParent() {
        return ExtraWood.class;
    }
}
