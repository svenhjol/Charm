package svenhjol.charm.feature.core.custom_pistons;

import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.core.custom_pistons.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

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
