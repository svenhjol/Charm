package svenhjol.charm.feature.endermite_powder;

import svenhjol.charm.feature.endermite_powder.common.Advancements;
import svenhjol.charm.feature.endermite_powder.common.Handlers;
import svenhjol.charm.feature.endermite_powder.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Endermites drop endermite powder that can be used to locate an End City.")
public final class EndermitePowder extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public EndermitePowder(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
