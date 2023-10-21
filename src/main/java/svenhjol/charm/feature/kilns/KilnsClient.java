package svenhjol.charm.feature.kilns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.firing.Firing;

public class KilnsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Kilns.class;
    }

    @Override
    public void preRegister() {
        var registry = mod().registry();
        registry.recipeBookCategoryEnum("kiln_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("kiln", Kilns.block);
    }

    @Override
    public void register() {
        var registry = mod().registry();
        registry.menuScreen(Kilns.menu, () -> KilnScreen::new);
        registry.recipeBookCategory("kiln", Firing.recipeType, Kilns.recipeBookType);

        if (isEnabled()) {
            registry.itemTab(Kilns.blockItem, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SMOKER);
        }
    }
}
