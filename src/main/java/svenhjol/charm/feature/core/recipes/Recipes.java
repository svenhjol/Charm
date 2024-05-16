package svenhjol.charm.feature.core.recipes;

import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.core.recipes.common.Handlers;
import svenhjol.charm.feature.core.recipes.common.Providers;
import svenhjol.charm.feature.core.recipes.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(priority = 90)
public final class Recipes extends CommonFeature implements SubFeature<Core> {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    public Recipes(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    @Override
    public Class<Core> typeForParent() {
        return Core.class;
    }
}
