package svenhjol.charm.module;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.enchantment.TintedEnchantment;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.handler.ColoredGlintHandler;

@Module(mod = Charm.MOD_ID, description = "When applied, this enchantment lets you change the color of the enchanted glint using dye on an anvil. Requires Core 'Enchantment glint override' to be true.")
public class Tinted extends CharmModule {
    public static TintedEnchantment TINTED;

    @Config(name = "XP cost", description = "Number of levels required to change a tinted item using dye on an anvil.")
    public static int xpCost = 0;

    @Override
    public void register() {
        TINTED = new TintedEnchantment(this);
    }

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
        EnchantmentsHelper.apply(stack, TINTED, 1);
        stack.getOrCreateTag().putString(ColoredGlintHandler.GLINT_TAG, color);
    }

    private ActionResult handleAnvilBehavior(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (!EnchantmentsHelper.has(left, TINTED) || !(right.getItem() instanceof DyeItem))
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
