package svenhjol.charm.feature.potion_of_radiance;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.potion_of_radiance.common.Registers;

@Feature(description = "Potion of Radiance gives the glowing effect. It is brewed using a torchflower.")
public final class PotionOfRadiance extends CommonFeature {
    public final Registers registers;

    public PotionOfRadiance(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
