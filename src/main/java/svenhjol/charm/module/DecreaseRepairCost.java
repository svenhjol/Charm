package svenhjol.charm.module;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Combine a tool or armor with a netherite nugget on an anvil to reduce its repair cost.")
public class DecreaseRepairCost extends CharmModule {
    @Config(name = "XP cost", description = "Number of levels required to reduce repair cost on the anvil.")
    public static int xpCost = 0;

    @Config(name = "Repair cost decrease", description = "The tool repair cost will be decreased by this amount.")
    public static int decreaseAmount = 5;

    @Override
    public void init() {
        // if anvil improvements are not enabled, then set the xpCost to 1.
        if (!ModuleHandler.enabled("charm:anvil_improvements") && xpCost < 1)
            xpCost = 1;

        // register the anvil recipe for this operation
        UpdateAnvilCallback.EVENT.register(this::tryReduceRepairCost);
    }

    private ActionResult tryReduceRepairCost(AnvilScreenHandler handler, ItemStack left, ItemStack right, Inventory output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out; // this will be the tool/armor with reduced repair cost

        if (left.isEmpty() || right.isEmpty())
            return ActionResult.PASS; // if both the input and middle items are empty, do nothing

        if (right.getItem() != NetheriteNuggets.NETHERITE_NUGGET)
            return ActionResult.PASS; // if the middle item is not a netherite nugget, do nothing

        if (left.getRepairCost() == 0)
            return ActionResult.PASS; // if the input item does not need repairing, do nothing

        // get the repair cost from the input item
        int cost = left.getRepairCost();

        // copy the input item to the output item and reduce the repair cost by the amount in the config
        out = left.copy();
        out.setRepairCost(Math.max(0, cost - decreaseAmount));

        // apply the stuff to the anvil
        apply.accept(out, xpCost, 1); // item to output, the xp cost of this operation, and the amount of nuggets used.

        return ActionResult.SUCCESS;
    }
}
