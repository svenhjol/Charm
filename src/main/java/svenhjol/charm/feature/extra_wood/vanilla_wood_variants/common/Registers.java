package svenhjol.charm.feature.extra_wood.vanilla_wood_variants.common;

import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.feature.extra_wood.vanilla_wood_variants.VanillaWoodVariants;
import svenhjol.charm.foundation.enums.VanillaWood;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.Collections;

public final class Registers extends RegisterHolder<VanillaWoodVariants> {
    public Registers(VanillaWoodVariants feature) {
        super(feature);

        var types = VanillaWood.getTypes();
        Collections.reverse(types);

        for (var variant : types) {
            var definition = new WoodDefinition(variant);
            CustomWood.register(feature, definition);
        }
    }
}
