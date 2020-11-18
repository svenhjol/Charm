package svenhjol.charm.module;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.handler.ColoredGlintHandler;
import svenhjol.charm.item.GlintBookItem;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, enabledByDefault = false, description = "Experimental, do not use!")
public class GlintBooks extends CharmModule {
    public static Map<String, GlintBookItem> GLINT_BOOKS = new HashMap<>();

    @Config(name = "XP cost", description = "Number of levels required to add the colored glint to an item.")
    public static int xpCost = 5;

    @Override
    public void register() {
        // register all 16 colored glint books
        for (DyeColor dyeColor : DyeColor.values()) {
            String color = dyeColor.asString();
            GLINT_BOOKS.put(color, new GlintBookItem(this, color));
        }
    }

    @Override
    public void init() {
        // register the anvil behavior
        UpdateAnvilCallback.EVENT.register(this::handleAnvilBehavior);
    }

    private ActionResult handleAnvilBehavior(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (!left.hasGlint() || !(right.getItem() instanceof GlintBookItem))
            return ActionResult.PASS;

        int cost = Math.max(0, xpCost);
        out = left.copy();

        String color = ColoredGlintHandler.getStackColor(right);
        ColoredGlintHandler.applyStackColor(out, color);

        apply.accept(out, cost, 1);
        return ActionResult.SUCCESS;
    }
}
