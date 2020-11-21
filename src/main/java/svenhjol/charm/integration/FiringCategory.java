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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.Kilns;

import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FiringCategory implements RecipeCategory<FiringDisplay> {
    @Override
    public List<Widget> setupDisplay(FiringDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        double cookingTime = display.getCookingTime();
        DecimalFormat df = new DecimalFormat("###.##");
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(Widgets.createBurningFire(new Point(startPoint.x + 1, startPoint.y + 20)).animationDurationMS(10000));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
            new TranslatableText("category.rei.cooking.time&xp", df.format(display.getXp()), df.format(cookingTime / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)).animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries(display.getResultingEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }

    @Override
    public Identifier getIdentifier() {
        return CharmReiPlugin.FIRING;
    }

    @Override
    public EntryStack getLogo() {
        return EntryStack.create(Kilns.KILN);
    }

    @Override
    public String getCategoryName() {
        return I18n.translate("rei." + Charm.MOD_ID + ".category.firing");
    }
}