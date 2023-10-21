package svenhjol.charm.feature.show_repair_cost;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.helper.TextHelper;

import java.util.List;

public class ShowRepairCost extends ClientFeature {
    @Override
    public String description() {
        return "An item's repair cost is shown in their tooltip when looking at the anvil screen.";
    }

    public static List<Component> addRepairCostToTooltip(ItemStack stack, List<Component> tooltip) {
        var repairCost = stack.getBaseRepairCost();
        if (repairCost > 0) {
            tooltip.add(TextHelper.empty()); // A blank line.
            tooltip.add(TextHelper.translatable("gui.charm.repair_cost", repairCost).withStyle(ChatFormatting.GRAY));
        }

        return tooltip;
    }
}
