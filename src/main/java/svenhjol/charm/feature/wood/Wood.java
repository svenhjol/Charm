package svenhjol.charm.feature.wood;

import svenhjol.charm.feature.wood.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.wood.common.Handlers;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWood;
import svenhjol.charm.feature.wood.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature(description = """
    Adds new wood types and variants.
    Disabling this feature will disable all related wood features.""")
public final class Wood extends CommonFeature {
    public final Handlers handlers;

    public Wood(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
    }

    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new AzaleaWood(loader()),
            new EbonyWood(loader()),
            new VanillaWoodVariants(loader())
        );
    }
}
