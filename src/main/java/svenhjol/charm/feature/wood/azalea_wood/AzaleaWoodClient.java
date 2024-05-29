package svenhjol.charm.feature.wood.azalea_wood;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.wood.WoodClient;
import svenhjol.charm.feature.wood.azalea_wood.client.Registers;

@Feature
public final class AzaleaWoodClient extends ClientFeature implements ChildFeature<WoodClient>, LinkedFeature<AzaleaWood> {
    public final Registers registers;

    public AzaleaWoodClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<AzaleaWood> typeForLinked() {
        return AzaleaWood.class;
    }

    @Override
    public Class<WoodClient> typeForParent() {
        return WoodClient.class;
    }
}
