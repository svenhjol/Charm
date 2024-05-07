package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charm.api.CharmApi;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.enums.VanillaWood;

public final class CommonRegistration extends Register<VanillaWoodVariants> {
    public CommonRegistration(VanillaWoodVariants feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        CharmApi.registerProvider(this);

        for (var variant : VanillaWood.getTypes()) {
            VariantWood.registerWood(feature.registry(), variant);
        }
    }
}
