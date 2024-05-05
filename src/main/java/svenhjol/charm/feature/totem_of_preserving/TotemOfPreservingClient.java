package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class TotemOfPreservingClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonClass() {
        return TotemOfPreserving.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
