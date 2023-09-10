package svenhjol.charm.feature.kilns;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import svenhjol.charmony.helper.TextHelper;

import java.util.Set;

public class KilnRecipeBookScreen extends AbstractFurnaceRecipeBookComponent {
    private static final Component NAME = TextHelper.translatable("gui.charm.recipebook.toggleRecipes.fireable");

    protected Component getRecipeFilterName() {
        return NAME;
    }

    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
