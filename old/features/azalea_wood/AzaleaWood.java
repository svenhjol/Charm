package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.feature.azalea_wood.common.Registers;
import svenhjol.charm.foundation.common.CommonFeature;

public class AzaleaWood extends CommonFeature {
    public static final String BOAT_ID = "charm_azalea";
    public static Registers registers;

    @Override
    public String description() {
        return "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.";
    }

    @Override
    public void setup() {
        registers = new Registers(this);
    }
}
