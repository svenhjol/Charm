package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class VanillaWoodVariants extends CommonFeature {
    @Override
    public String description() {
        return "Barrels, bookcases, chests and ladders in all vanilla wood types.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
