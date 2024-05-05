package svenhjol.charm.feature.kilns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.foundation.Registration;

public final class ClientRegistration extends Registration<KilnsClient> {
    public ClientRegistration(KilnsClient feature) {
        super(feature);

        // Early registration of enums.
        var registry = feature.registry();
        registry.recipeBookCategoryEnum("kiln_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("kiln", Kilns.block);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        registry.menuScreen(Kilns.menu, () -> KilnScreen::new);
        registry.recipeBookCategory("kiln", Firing.recipeType, Kilns.recipeBookType);
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(Kilns.blockItem, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SMOKER);
    }
}
