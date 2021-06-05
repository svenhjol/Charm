package svenhjol.charm.integration.rei;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.DisplayRenderer;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.SimpleDisplayRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.TransferDisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FiringCategory implements TransferDisplayCategory<FiringDisplay> {
    private CategoryIdentifier<? extends FiringDisplay> identifier;
    private EntryStack<?> logo;
    private String categoryName;

    public FiringCategory(CategoryIdentifier<? extends FiringDisplay> identifier, EntryStack<?> logo, String categoryName) {
        this.identifier = identifier;
        this.logo = logo;
        this.categoryName = categoryName;
    }

    @Override
    public void renderRedSlots(MatrixStack matrices, List<Widget> widgets, Rectangle bounds, FiringDisplay display, IntList redSlots) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        matrices.push();
        matrices.translate(0, 0, 400);
        if (redSlots.contains(0)) {
            DrawableHelper.fill(matrices, startPoint.x + 1, startPoint.y + 1, startPoint.x + 1 + 16, startPoint.y + 1 + 16, 1090453504);
        }
        matrices.pop();
    }

    @Override
    public List<Widget> setupDisplay(FiringDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        double cookingTime = display.getCookingTime();
        DecimalFormat df = new DecimalFormat("###.##");
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(Widgets.createBurningFire(new Point(startPoint.x + 1, startPoint.y + 20))
            .animationDurationMS(10000));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
            new TranslatableText("category.rei.cooking.time&xp", df.format(display.getXp()), df.format(cookingTime / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8))
            .animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9))
            .entries(display.getOutputEntries().get(0))
            .disableBackground()
            .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1))
            .entries(display.getInputEntries().get(0))
            .markInput());
        return widgets;
    }

    @Override
    public DisplayRenderer getDisplayRenderer(FiringDisplay display) {
        return SimpleDisplayRenderer.from(Collections.singletonList(display.getInputEntries().get(0)), display.getOutputEntries());
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }

    @Override
    public CategoryIdentifier<? extends FiringDisplay> getCategoryIdentifier() {
        return identifier;
    }

    @Override
    public Renderer getIcon() {
        return logo;
    }

    @Override
    public Text getTitle() {
        return new TranslatableText(categoryName);
    }
}