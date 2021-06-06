package svenhjol.charm.module.kilns;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class KilnRecipeBookScreen extends AbstractFurnaceRecipeBookComponent {
    private static final Component text = new TranslatableComponent("gui.charm.recipebook.toggleRecipes.fireable");

    protected Component getRecipeFilterName() {
        return text;
    }

    protected Set<Item> getFuelItems() {
        return AbstractFurnaceBlockEntity.getFuel().keySet();
    }
}
