package svenhjol.charm.feature.core.custom_advancements;

import svenhjol.charm.feature.core.Core;
import svenhjol.charm.feature.core.custom_advancements.common.Handlers;
import svenhjol.charm.feature.core.custom_advancements.common.Providers;
import svenhjol.charm.feature.core.custom_advancements.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature(priority = 80)
public final class CustomAdvancements extends CommonFeature implements SubFeature<Core> {
    public final Registers registers;
    public final Providers providers;
    public final Handlers handlers;

    public CustomAdvancements(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
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
