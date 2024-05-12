package svenhjol.charm.feature.variant_pistons;

import svenhjol.charm.feature.variant_pistons.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature
public final class VariantPistons extends CommonFeature {
    public final Handlers handlers;

    public VariantPistons(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }
}
