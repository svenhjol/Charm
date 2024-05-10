package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.feature.variant_wood.client.Handlers;
import svenhjol.charm.feature.variant_wood.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.enums.Side;

@Feature(side = Side.CLIENT, canBeDisabled = false)
public class VariantWoodClient extends ClientFeature {
    public final VariantWood common;
    public final Registers registers;
    public final Handlers handlers;

    public VariantWoodClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(VariantWood.class);
        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
