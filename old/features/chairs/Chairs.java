package svenhjol.charm.feature.chairs;

import svenhjol.charm.feature.chairs.common.Advancements;
import svenhjol.charm.feature.chairs.common.Handlers;
import svenhjol.charm.feature.chairs.common.Registers;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.Setup;

/**
 * Inspired by Quark's SitInStairs module.
 */
public class Chairs extends CommonFeature {
    public final Setup<Advancements> advancements;
    public final Setup<Registers> registers;
    public final Setup<Handlers> handlers;

    public Chairs(Loader<? extends Feature> loader) {
        super(loader);
        advancements = Setup.of(this, Advancements::new);
        registers = Setup.of(this, Registers::new);
        handlers = Setup.of(this, Handlers::new);
    }

    @Override
    public String description() {
        return "Right-click (with empty hand) on any stairs block to sit down.";
    }
}
