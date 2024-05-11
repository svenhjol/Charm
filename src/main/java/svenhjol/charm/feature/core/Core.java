package svenhjol.charm.feature.core;

import svenhjol.charm.feature.core.common.Advancements;
import svenhjol.charm.feature.core.common.Handlers;
import svenhjol.charm.feature.core.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(priority = 100)
public class Core extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public Core(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }
}
