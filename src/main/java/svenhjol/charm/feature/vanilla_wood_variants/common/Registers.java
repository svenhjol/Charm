package svenhjol.charm.feature.vanilla_wood_variants.common;

import svenhjol.charm.feature.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.enums.VanillaWood;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<VanillaWoodVariants> {
    public Registers(VanillaWoodVariants feature) {
        super(feature);

        for (var variant : VanillaWood.getTypes()) {
            VariantWood.register(feature, variant);
        }
    }
}
