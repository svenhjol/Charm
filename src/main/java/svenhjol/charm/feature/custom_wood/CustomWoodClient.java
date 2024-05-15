package svenhjol.charm.feature.custom_wood;

import svenhjol.charm.feature.custom_wood.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature(priority = -10)
public final class CustomWoodClient extends ClientFeature implements CommonResolver<CustomWood> {
    public final Registers registers;

    public CustomWoodClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<CustomWood> typeForCommon() {
        return CustomWood.class;
    }
}
