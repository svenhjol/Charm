package svenhjol.charm.feature.coral_sea_lanterns;

import svenhjol.charm.feature.coral_sea_lanterns.common.Providers;
import svenhjol.charm.feature.coral_sea_lanterns.common.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Coral can be combined with sea lanterns to make colored variants.")
public final class CoralSeaLanterns extends CommonFeature  {
    public final Registers registers;
    public final Providers providers;

    public CoralSeaLanterns(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }
}
