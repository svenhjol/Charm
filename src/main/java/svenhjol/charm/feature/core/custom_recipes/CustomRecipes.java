package svenhjol.charm.feature.core.custom_recipes;

import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.core.custom_recipes.common.Handlers;
import svenhjol.charm.feature.core.custom_recipes.common.Providers;
import svenhjol.charm.feature.core.custom_recipes.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(priority = 90)
public final class CustomRecipes extends CommonFeature implements ChildFeature<Core> {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    public CustomRecipes(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public Class<Core> typeForParent() {
        return Core.class;
    }
}
