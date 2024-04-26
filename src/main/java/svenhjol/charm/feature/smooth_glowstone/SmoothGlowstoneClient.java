package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class SmoothGlowstoneClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return SmoothGlowstone.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
