package svenhjol.charm.feature.chairs;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class ChairsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Chairs.class;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new ClientRegister(this));
    }
}
