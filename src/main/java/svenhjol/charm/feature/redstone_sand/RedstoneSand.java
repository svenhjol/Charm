package svenhjol.charm.feature.redstone_sand;

import svenhjol.charm.feature.redstone_sand.common.Registers;
import svenhjol.charm.feature.redstone_sand.common.Providers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "A block that acts like sand but is powered like a block of redstone.")
public final class RedstoneSand extends CommonFeature {
    public final Registers registers;
    public final Providers providers;

    public RedstoneSand(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }
}
