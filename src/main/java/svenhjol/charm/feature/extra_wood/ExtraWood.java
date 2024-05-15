package svenhjol.charm.feature.extra_wood;

import svenhjol.charm.feature.extra_wood.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.extra_wood.common.Handlers;
import svenhjol.charm.feature.extra_wood.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

import java.util.List;

@Feature
public final class ExtraWood extends CommonFeature {
    public final Handlers handlers;

    public ExtraWood(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public List<? extends SubFeature<? extends svenhjol.charm.foundation.Feature>> subFeatures() {
        return List.of(
            new AzaleaWood(loader()),
            new VanillaWoodVariants(loader())
        );
    }
}
