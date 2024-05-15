package svenhjol.charm.feature.extra_wood.vanilla_wood_variants.common;

import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.extra_wood.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.foundation.enums.VanillaWood;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<VanillaWoodVariants> {
    public Registers(VanillaWoodVariants feature) {
        super(feature);

        for (var variant : VanillaWood.getTypes()) {
            var definition = new WoodDefinition(variant);
            CustomWood.register(feature, definition);
        }
    }
}
