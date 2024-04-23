package svenhjol.charm.feature.smooth_glowstone;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class SmoothGlowstoneClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return SmoothGlowstone.class;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new RegisterClient(this));
    }
}
