package svenhjol.charm.feature.vanilla_wood_chiseled_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_chiseled_bookshelves.VariantChiseledBookshelves;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

@Feature(mod = Charm.MOD_ID, description = "Chiseled Bookshelves in all vanilla wood types.")
public class VanillaWoodChiseledBookshelves extends CharmFeature {
    @Override
    public void register() {
        var registry = Charm.instance().registry();

        for (var material : VanillaWood.getTypes()) {
            VariantChiseledBookshelves.registerChiseledBookshelf(registry, material);
        }
    }
}
