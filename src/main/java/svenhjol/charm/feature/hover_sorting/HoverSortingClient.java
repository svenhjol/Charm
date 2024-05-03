package svenhjol.charm.feature.hover_sorting;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class HoverSortingClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonClass() {
        return HoverSorting.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
