package svenhjol.charm.feature.chairs;

import svenhjol.charm.feature.chairs.common.Advancements;
import svenhjol.charm.feature.chairs.common.Handlers;
import svenhjol.charm.feature.chairs.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

/**
 * Inspired by Quark's SitInStairs module.
 */
@Feature(description = "Right-click (with empty hand) on any stairs block to sit down.")
public class Chairs extends CommonFeature {
    public final Advancements advancements;
    public final Registers registers;
    public final Handlers handlers;

    public Chairs(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
