package svenhjol.charm.feature.extra_wood.azalea_wood;

import svenhjol.charm.feature.extra_wood.ExtraWoodClient;
import svenhjol.charm.feature.extra_wood.azalea_wood.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature
public final class AzaleaWoodClient extends ClientFeature implements SubFeature<ExtraWoodClient>, CommonResolver<AzaleaWood> {
    public final Registers registers;

    public AzaleaWoodClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<AzaleaWood> typeForCommon() {
        return AzaleaWood.class;
    }

    @Override
    public Class<ExtraWoodClient> typeForParent() {
        return ExtraWoodClient.class;
    }
}
