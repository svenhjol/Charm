package svenhjol.charm.base.integration;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.Woodcutters;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WoodcuttingCategory implements RecipeCategory<WoodcuttingDisplay> {
    @Override
    public List<Widget> setupDisplay(WoodcuttingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width / 2, bounds.y + 9), new TranslatableText("rei." + Charm.MOD_ID + ".category.woodcutting")).noShadow().centered().color(0xFF404040, 0xFFBBBBBB));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 25)).animationDurationTicks(100));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 26)).entries(display.getInputEntries().get(0)).markInput());

        int outputX = startPoint.x + 61;
        int outputY = startPoint.y + 26;
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 26)));
        widgets.add(Widgets.createSlot(new Point(outputX, outputY)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 66;
    }

    @Override
    public Identifier getIdentifier() {
        return Woodcutters.RECIPE_ID;
    }

    @Override
    public EntryStack getLogo() {
        return EntryStack.create(Woodcutters.WOODCUTTER);
    }

    @Override
    public String getCategoryName() {
        return I18n.translate("rei." + Charm.MOD_ID + ".category.woodcutting");
    }

    public static void register(RecipeHelper recipeHelper) {
        // TODO: iterate woodcutting recipes
        recipeHelper.registerDisplay(new WoodcuttingDisplay(new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.OAK_BOAT)));
        recipeHelper.registerDisplay(new WoodcuttingDisplay(new ItemStack(Items.OAK_PLANKS), new ItemStack(Items.OAK_FENCE)));
    }
}