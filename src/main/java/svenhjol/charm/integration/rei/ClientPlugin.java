package svenhjol.charm.integration.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import svenhjol.charm.feature.firing.common.FiringRecipe;
import svenhjol.charm.feature.kilns.client.Screen;
import svenhjol.charm.feature.woodcutting.common.WoodcuttingRecipe;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "unsafe", "RedundantSuppression"})
public final class ClientPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(WoodcuttingRecipe.class, Definitions.woodcutting().registers.recipeType.get(), WoodcuttingDisplay::new);
        registry.registerRecipeFiller(FiringRecipe.class, Definitions.firing().registers.recipeType.get(), FiringDisplay::new);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(
            new WoodcuttingCategory(),
            new FiringCategory(Definitions.FIRING, EntryStacks.of(Definitions.kilns().registers.block.get()), "category.rei.charm.firing")
        );

        registry.addWorkstations(Definitions.WOODCUTTING, EntryStacks.of(Definitions.woodcutters().registers.block.get()));
        registry.addWorkstations(Definitions.FIRING, EntryStacks.of(Definitions.kilns().registers.block.get()));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(78, 32, 28, 23), Screen.class, Definitions.FIRING);
    }
}
