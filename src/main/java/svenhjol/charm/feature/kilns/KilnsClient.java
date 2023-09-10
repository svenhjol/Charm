package svenhjol.charm.feature.kilns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.feature.firing.Firing;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class KilnsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(Kilns.class));
    }

    @Override
    public void preRegister() {
        var registry = CharmClient.instance().registry();
        registry.recipeBookCategoryEnum("kiln_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("kiln", Kilns.block);
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();
        registry.menuScreen(Kilns.menu, () -> KilnScreen::new);
        registry.recipeBookCategory("kiln", Firing.recipeType, Kilns.recipeBookType);

        if (isEnabled()) {
            registry.itemTab(Kilns.blockItem, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SMOKER);
        }
    }
}
