package svenhjol.charm.feature.woodcutters;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.Registration;

public class ClientRegistration extends Registration<WoodcuttersClient> {
    public ClientRegistration(WoodcuttersClient feature) {
        super(feature);

        var registry = feature.registry();
        registry.recipeBookCategoryEnum("woodcutter_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("woodcutter", Woodcutters.block);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        registry.menuScreen(Woodcutters.menu, () -> WoodcutterScreen::new);
        registry.recipeBookCategory("woodcutter", Woodcutting.recipeType, Woodcutters.recipeBookType);

        // The woodcutter block should render transparent areas cut out.
        registry.blockRenderType(Woodcutters.block, RenderType::cutout);
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(Woodcutters.block, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.STONECUTTER);
    }
}
