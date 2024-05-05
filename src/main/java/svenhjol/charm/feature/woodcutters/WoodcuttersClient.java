package svenhjol.charm.feature.woodcutters;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class WoodcuttersClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonClass() {
        return Woodcutters.class;
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
