package svenhjol.charm.feature.redstone_sand;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.redstone_sand.common.Providers;
import svenhjol.charm.feature.redstone_sand.common.Registers;

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
