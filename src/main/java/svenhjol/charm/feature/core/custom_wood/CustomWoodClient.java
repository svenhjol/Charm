package svenhjol.charm.feature.core.custom_wood;

import svenhjol.charm.feature.core.CoreClient;
import svenhjol.charm.feature.core.custom_wood.client.Handlers;
import svenhjol.charm.feature.core.custom_wood.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature(priority = -10)
public final class CustomWoodClient extends ClientFeature implements ChildFeature<CoreClient>, CommonResolver<CustomWood> {
    public final Registers registers;
    public final Handlers handlers;

    public CustomWoodClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<CustomWood> typeForCommon() {
        return CustomWood.class;
    }

    @Override
    public Class<CoreClient> typeForParent() {
        return CoreClient.class;
    }
}
