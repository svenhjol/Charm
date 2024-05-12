package svenhjol.charm.feature.kilns.client;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import java.util.Set;

public class RecipeBookScreen extends AbstractFurnaceRecipeBookComponent {
    private static final Component NAME = Component.translatable("gui.charm.recipebook.toggleRecipes.fireable");

    protected Component getRecipeFilterName() {
        return NAME;
    }

    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
