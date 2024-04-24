package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class VanillaWoodVariants extends CommonFeature {
    @Override
    public String description() {
        return "Barrels, bookcases, chests and ladders in all vanilla wood types.";
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegister(this));
    }
}
