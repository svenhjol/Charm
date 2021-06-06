package svenhjol.charm.integration.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import svenhjol.charm.integration.rei.CharmReiCategories;
import svenhjol.charm.integration.rei.FiringCategory;
import svenhjol.charm.integration.rei.FiringDisplay;
import svenhjol.charm.integration.rei.WoodcuttingCategory;
import svenhjol.charm.integration.rei.WoodcuttingDisplay;
import svenhjol.charm.module.kilns.FiringRecipe;
import svenhjol.charm.module.kilns.KilnScreen;
import svenhjol.charm.module.kilns.Kilns;
import svenhjol.charm.module.woodcutters.Woodcutters;
import svenhjol.charm.module.woodcutters.WoodcuttingRecipe;

@Environment(EnvType.CLIENT)
public class CharmReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(WoodcuttingRecipe.class, WoodcuttingDisplay::new);
        registry.registerFiller(FiringRecipe.class, FiringDisplay::new);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(
            new WoodcuttingCategory(),
            new FiringCategory(svenhjol.charm.integration.rei.CharmReiCategories.FIRING, EntryStacks.of(Kilns.KILN), "rei.charm.category.firing")
        );

        registry.addWorkstations(svenhjol.charm.integration.rei.CharmReiCategories.WOODCUTTING, EntryStacks.of(Woodcutters.WOODCUTTER));
        registry.addWorkstations(svenhjol.charm.integration.rei.CharmReiCategories.FIRING, EntryStacks.of(Kilns.KILN));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23), KilnScreen.class, CharmReiCategories.FIRING);
    }
}
