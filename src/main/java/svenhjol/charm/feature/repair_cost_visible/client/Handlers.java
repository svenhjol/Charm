package svenhjol.charm.feature.repair_cost_visible.client;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.repair_cost_visible.RepairCostVisible;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<RepairCostVisible> {
    public Handlers(RepairCostVisible feature) {
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
