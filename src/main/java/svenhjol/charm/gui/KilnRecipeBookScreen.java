package svenhjol.charm.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class KilnRecipeBookScreen extends AbstractFurnaceRecipeBookScreen {
    private static final Text text = new TranslatableText("gui.charm.recipebook.toggleRecipes.fireable");

    protected Text getToggleCraftableButtonText() {
        return text;
    }

    protected Set<Item> getAllowedFuels() {
        return AbstractFurnaceBlockEntity.createFuelTimeMap().keySet();
    }
}
