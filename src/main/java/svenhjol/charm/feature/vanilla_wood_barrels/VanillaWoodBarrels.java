package svenhjol.charm.feature.vanilla_wood_barrels;

import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IVariantBarrelProvider;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Barrels in all vanilla wood types.")
public class VanillaWoodBarrels extends CharmFeature implements IVariantBarrelProvider {
    @Override
    public void register() {
        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<IVariantMaterial> getVariantBarrels() {
        return new ArrayList<>(VanillaWood.getTypes());
    }
}
