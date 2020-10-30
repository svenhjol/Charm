package svenhjol.charm.module;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.handler.ColoredGlintHandler;

@Module(mod = Charm.MOD_ID, alwaysEnabled = true, description = "Upcoming feature for 2.1.2")
public class Moonstones extends CharmModule {
    @Override
    public void init() {
        UpdateAnvilCallback.EVENT.register(this::tryColor);
    }

    private ActionResult tryColor(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out;

        if (!Core.debug)
            return ActionResult.PASS;

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS;

        if (!(right.getItem() instanceof DyeItem))
            return ActionResult.PASS;

        ListTag enchantments = left.getEnchantments();
        if (enchantments.isEmpty())
            return ActionResult.PASS;

        DyeItem dye = (DyeItem)right.getItem();
        String colorName = dye.getColor().asString();

        out = left.copy();
        out.getOrCreateTag().putString(ColoredGlintHandler.GLINT_TAG, colorName);
        apply.accept(out, 0, 1);

        return ActionResult.SUCCESS;
    }
}
