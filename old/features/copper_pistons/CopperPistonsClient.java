package svenhjol.charm.feature.copper_pistons;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class CopperPistonsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> relatedCommonFeature() {
        return CopperPistons.class;
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}