package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class AzaleaWoodClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return AzaleaWood.class;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new ClientRegister(this));
    }
}
