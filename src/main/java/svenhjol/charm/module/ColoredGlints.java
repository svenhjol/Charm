package svenhjol.charm.module;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.handler.ColoredGlintHandler;

@Module(mod = Charm.MOD_ID, description = "When applied, you may use dyes on an anvil to change the item's enchantment color. Requires Core 'Enchantment glint override' to be true.")
public class ColoredGlints extends CharmModule {

    @Config(name = "XP cost", description = "Number of levels of XP required to change an item's enchantment color using dye on an anvil.")
    public static int xpCost = 0;

    @Override
    public boolean depends() {
        return Core.overrideGlint;
    }

    @Override
    public void init() {
        if (!ModuleHandler.enabled("charm:anvil_improvements") && xpCost < 1)
            xpCost = 1;

        // listen for anvil behavior
        UpdateAnvilCallback.EVENT.register(this::handleAnvilBehavior);
    }

    /**
     * Adds the enchantment and color directly to the input stack with no sanity checking.
     */
    public static void applyTint(ItemStack stack, String color) {
        stack.getOrCreateTag().putString(ColoredGlintHandler.GLINT_TAG, color);
    }

    private ActionResult handleAnvilBehavior(AnvilScreenHandler handler, PlayerEntity player, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (EnchantmentHelper.get(left).size() == 0 || !(right.getItem() instanceof DyeItem))
            return ActionResult.PASS;

        int cost = Math.max(0, xpCost);
        out = left.copy();
        DyeItem dye = (DyeItem)right.getItem();
        String color = dye.getColor().asString();

        applyTint(out, color);
        apply.accept(out, cost, 1);

        return ActionResult.SUCCESS;
    }
}
