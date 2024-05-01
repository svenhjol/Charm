package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class CustomWoodClient extends ClientFeature {
    @Override
    public int priority() {
        return -10;
    }

    @Override
    public Class<? extends CommonFeature> commonClass() {
        return CustomWood.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
