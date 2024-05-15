package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.feature.variant_wood.client.Handlers;
import svenhjol.charm.feature.variant_wood.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

// TODO: move this to custom_wood
@Feature
public final class VariantWoodClient extends ClientFeature implements CommonResolver<VariantWood> {
    public final Registers registers;
    public final Handlers handlers;

    public VariantWoodClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<VariantWood> typeForCommon() {
        return VariantWood.class;
    }
}
