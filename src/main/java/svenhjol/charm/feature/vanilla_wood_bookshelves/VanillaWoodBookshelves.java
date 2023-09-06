package svenhjol.charm.feature.vanilla_wood_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.api.IVariantBookshelfProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Bookshelves in all vanilla wood types.")
public class VanillaWoodBookshelves extends CharmFeature implements IVariantBookshelfProvider {
    @Override
    public void register() {
        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<IVariantMaterial> getVariantBookshelves() {
        return new ArrayList<>(VanillaWood.getTypes());
    }
}
