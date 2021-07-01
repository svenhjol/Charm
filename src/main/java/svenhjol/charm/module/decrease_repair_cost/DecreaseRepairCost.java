package svenhjol.charm.module.decrease_repair_cost;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.util.TriConsumer;
import svenhjol.charm.Charm;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.event.TakeAnvilOutputCallback;
import svenhjol.charm.event.UpdateAnvilCallback;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.mixin.accessor.ItemCombinerMenuAccessor;

@Module(mod = Charm.MOD_ID, description = "Combine a tool or armor with an amethyst shard on an anvil to reduce its repair cost.")
public class DecreaseRepairCost extends CommonModule {
    public static final ResourceLocation TRIGGER_DECREASED_COST = new ResourceLocation(Charm.MOD_ID, "decreased_cost");

    @Config(name = "XP cost", description = "Number of levels required to reduce repair cost on the anvil.")
    public static int xpCost = 0;

    @Config(name = "Repair cost decrease", description = "The tool repair cost will be decreased by this amount.")
    public static int decreaseAmount = 5;

    @Override
    public void init() {
        // if anvil improvements are not enabled, then set the xpCost to 1.
        if (!Charm.LOADER.isEnabled("anvil_improvements") && xpCost < 1)
            xpCost = 1;

        // register the anvil recipe for this operation
        UpdateAnvilCallback.EVENT.register(this::tryReduceRepairCost);

        // listen for when player takes item from anvil
        TakeAnvilOutputCallback.EVENT.register(this::handleTakeOutput);
    }

    private InteractionResult tryReduceRepairCost(AnvilMenu handler, Player player, ItemStack left, ItemStack right, Container output, String name, int baseCost, TriConsumer<ItemStack, Integer, Integer> apply) {
        ItemStack out; // this will be the tool/armor with reduced repair cost

        if (left.isEmpty() || right.isEmpty())
            return InteractionResult.PASS; // if both the input and middle items are empty, do nothing

        if (right.getItem() != Items.AMETHYST_SHARD)
            return InteractionResult.PASS; // if the middle item is not an amethyst shard, do nothing

        if (left.getBaseRepairCost() == 0)
            return InteractionResult.PASS; // if the input item does not need repairing, do nothing

        // get the repair cost from the input item
        int cost = left.getBaseRepairCost();

        // copy the input item to the output item and reduce the repair cost by the amount in the config
        out = left.copy();
        out.setRepairCost(Math.max(0, cost - decreaseAmount));

        // apply the stuff to the anvil
        apply.accept(out, xpCost, 1); // item to output, the xp cost of this operation, and the amount of shards used.

        return InteractionResult.SUCCESS;
    }

    private void handleTakeOutput(AnvilMenu handler, Player player, ItemStack stack) {
        if (!player.level.isClientSide) {
            Container input = ((ItemCombinerMenuAccessor) handler).getInputSlots();
            if (!input.isEmpty()
                && !input.getItem(0).isEmpty()
                && input.getItem(0).getBaseRepairCost() > stack.getBaseRepairCost()
            ) {
                triggerDecreasedCost((ServerPlayer) player);
            }
        }
    }

    public static void triggerDecreasedCost(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_DECREASED_COST);
    }
}
