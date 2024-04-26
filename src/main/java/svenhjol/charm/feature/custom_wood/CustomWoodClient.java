package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;

public class CustomWoodClient extends ClientFeature {
    @Override
    public int priority() {
        return -10;
    }

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return CustomWood.class;
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new ClientRegister(this));
    }
}
