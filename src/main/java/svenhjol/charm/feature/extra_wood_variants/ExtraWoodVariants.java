package svenhjol.charm.feature.extra_wood_variants;

import svenhjol.charm.feature.extra_wood_variants.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.extra_wood_variants.common.Handlers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.SubFeature;

import java.util.List;

@Feature
public final class ExtraWoodVariants extends CommonFeature {
    public final Handlers handlers;

    public ExtraWoodVariants(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public List<? extends SubFeature<? extends svenhjol.charm.foundation.Feature>> subFeatures() {
        return List.of(
            new AzaleaWood(loader())
        );
    }
}
