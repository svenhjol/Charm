package svenhjol.charm.feature.show_repair_cost.client;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.show_repair_cost.ShowRepairCost;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<ShowRepairCost> {
    public Handlers(ShowRepairCost feature) {
        super(feature);
    }

    public List<Component> addRepairCostToTooltip(ItemStack stack, List<Component> tooltip) {
        var repairCost = stack.get(DataComponents.REPAIR_COST);
        if (repairCost != null && repairCost > 0) {
            tooltip.add(Component.empty()); // A blank line.
            tooltip.add(Component.translatable("gui.charm.repair_cost", repairCost)
                .withStyle(ChatFormatting.GRAY));
        }

        return tooltip;
    }
}
