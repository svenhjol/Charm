package svenhjol.charm.feature.show_repair_cost;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.TextHelper;

import java.util.List;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "An item's repair cost is shown in their tooltip when looking at the anvil screen."
)
public class ShowRepairCost extends CharmFeature {
    public static List<Component> addRepairCostToTooltip(ItemStack stack, List<Component> tooltip) {
        var repairCost = stack.getBaseRepairCost();
        if (repairCost > 0) {
            tooltip.add(TextHelper.empty()); // A blank line.
            tooltip.add(TextHelper.translatable("gui.charm.repair_cost", repairCost).withStyle(ChatFormatting.GRAY));
        }

        return tooltip;
    }
}
