package svenhjol.charm.integration.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import svenhjol.charm.feature.kilns.KilnScreen;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.feature.firing.Firing;
import svenhjol.charmony.feature.firing.FiringRecipe;
import svenhjol.charmony.feature.woodcutting.Woodcutting;
import svenhjol.charmony.feature.woodcutting.WoodcuttingRecipe;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"UnstableApiUsage", "unchecked", "unsafe", "RedundantSuppression"})
public class CharmReiClientPlugin implements REIClientPlugin, ICharmReiClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(WoodcuttingRecipe.class, Woodcutting.recipeType.get(), WoodcuttingDisplay::new);
        registry.registerRecipeFiller(FiringRecipe.class, Firing.recipeType.get(), FiringDisplay::new);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(
            new WoodcuttingCategory(),
            new FiringCategory(FIRING, EntryStacks.of(Kilns.block.get()), "category.rei.charm.firing")
        );

        registry.addWorkstations(WOODCUTTING, EntryStacks.of(Woodcutters.block.get()));
        registry.addWorkstations(FIRING, EntryStacks.of(Kilns.block.get()));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23), KilnScreen.class, ICharmReiPlugin.FIRING);
    }
}
