package svenhjol.charm.feature.woodcutters;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.feature.woodcutting.Woodcutting;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class WoodcuttersClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(Woodcutters.class));
    }

    @Override
    public void preRegister() {
        var registry = CharmClient.instance().registry();
        registry.recipeBookCategoryEnum("woodcutter_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("woodcutter", Woodcutters.BLOCK);
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();
        registry.menuScreen(Woodcutters.MENU, () -> WoodcutterScreen::new);
        registry.recipeBookCategory("woodcutter", Woodcutting.recipeType, Woodcutters.RECIPE_BOOK_TYPE);

        // The woodcutter block should render transparent areas cut out.
        registry.blockRenderType(Woodcutters.BLOCK, RenderType::cutout);

        if (isEnabled()) {
            registry.itemTab(Woodcutters.BLOCK, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.STONECUTTER);
        }
    }
}
