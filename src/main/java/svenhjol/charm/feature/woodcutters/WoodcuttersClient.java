package svenhjol.charm.feature.woodcutters;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class WoodcuttersClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Woodcutters.class;
    }

    @Override
    public void preRegister() {
        var registry = mod().registry();
        registry.recipeBookCategoryEnum("woodcutter_search", () -> Items.COMPASS);
        registry.recipeBookCategoryEnum("woodcutter", Woodcutters.block);
    }

    @Override
    public void register() {
        var registry = mod().registry();
        registry.menuScreen(Woodcutters.menu, () -> WoodcutterScreen::new);
        registry.recipeBookCategory("woodcutter", Woodcutting.recipeType, Woodcutters.recipeBookType);

        // The woodcutter block should render transparent areas cut out.
        registry.blockRenderType(Woodcutters.block, RenderType::cutout);

        if (isEnabled()) {
            registry.itemTab(Woodcutters.block, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.STONECUTTER);
        }
    }
}
