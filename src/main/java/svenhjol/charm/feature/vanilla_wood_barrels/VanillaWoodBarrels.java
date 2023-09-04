package svenhjol.charm.feature.vanilla_wood_barrels;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

@Feature(mod = Charm.MOD_ID, description = "Barrels in all vanilla wood types.")
public class VanillaWoodBarrels extends CharmFeature {
    @Override
    public void register() {
        var registry = Charm.instance().registry();

        for (var material : VanillaWood.getTypes()) {
            VariantBarrels.registerBarrel(registry, material);
        }
    }
}
