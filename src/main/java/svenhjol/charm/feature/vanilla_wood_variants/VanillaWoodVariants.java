package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.enums.VanillaWood;
import svenhjol.charmony.feature.variant_wood.VariantWood;
import svenhjol.charmony_api.CharmonyApi;

public class VanillaWoodVariants extends CommonFeature {
    @Override
    public String description() {
        return "Barrels, bookcases, chests and ladders in all vanilla wood types.";
    }

    @Override
    public void register() {
        CharmonyApi.registerProvider(this);

        for (var variant : VanillaWood.getTypes()) {
            VariantWood.registerWood(mod().registry(), variant);
        }
    }
}
