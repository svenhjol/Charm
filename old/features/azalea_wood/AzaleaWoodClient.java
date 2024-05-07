package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.feature.azalea_wood.client.Registers;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

public class AzaleaWoodClient extends ClientFeature {
    public static Registers registers;
    @Override
    public Class<? extends CommonFeature> relatedCommonFeature() {
        return AzaleaWood.class;
    }

    @Override
    public void setup() {
        registers = new Registers(this);
    }
}
