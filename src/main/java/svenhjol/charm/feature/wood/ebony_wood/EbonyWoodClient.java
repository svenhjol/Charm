package svenhjol.charm.feature.wood.ebony_wood;

import svenhjol.charm.feature.wood.WoodClient;
import svenhjol.charm.feature.wood.ebony_wood.client.Registers;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;

@Feature
public final class EbonyWoodClient extends ClientFeature implements ChildFeature<WoodClient> {
    public final Registers registers;

    public EbonyWoodClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<WoodClient> typeForParent() {
        return WoodClient.class;
    }
}
