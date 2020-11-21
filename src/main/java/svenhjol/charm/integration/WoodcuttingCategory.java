package svenhjol.charm.integration;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.Woodcutters;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WoodcuttingCategory implements RecipeCategory<WoodcuttingDisplay> {
    @Override
    public List<Widget> setupDisplay(WoodcuttingDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public Identifier getIdentifier() {
        return CharmReiPlugin.WOODCUTTING;
    }

    @Override
    public EntryStack getLogo() {
        return EntryStack.create(Woodcutters.WOODCUTTER);
    }

    @Override
    public String getCategoryName() {
        return I18n.translate("rei." + Charm.MOD_ID + ".category.woodcutting");
    }
}