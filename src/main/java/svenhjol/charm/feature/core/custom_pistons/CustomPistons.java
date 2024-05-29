package svenhjol.charm.feature.core.custom_pistons;

import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.core.custom_pistons.common.Handlers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature
public final class CustomPistons extends CommonFeature implements ChildFeature<Core> {
    public final Handlers handlers;

    public CustomPistons(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
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
