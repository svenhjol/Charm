package svenhjol.charm.feature.extra_wood_variants.azalea_wood;

import svenhjol.charm.feature.extra_wood_variants.ExtraWoodVariantsClient;
import svenhjol.charm.feature.extra_wood_variants.azalea_wood.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;
import svenhjol.charm.foundation.feature.SubFeature;

@Feature
public final class AzaleaWoodClient extends ClientFeature implements SubFeature<ExtraWoodVariantsClient>, CommonResolver<AzaleaWood> {
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
    public Class<ExtraWoodVariantsClient> typeForParent() {
        return ExtraWoodVariantsClient.class;
    }
}
