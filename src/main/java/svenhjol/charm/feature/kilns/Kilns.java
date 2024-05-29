package svenhjol.charm.feature.kilns;

import svenhjol.charm.feature.kilns.common.Advancements;
import svenhjol.charm.feature.kilns.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "A functional block that speeds up cooking of clay, glass, bricks and terracotta.")
public final class Kilns extends CommonFeature {
    public final Registers registers;
    public final Advancements advancements;

    public Kilns(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
    }
}
