package svenhjol.charm.module.show_repair_cost;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "An item's repair cost is shown in their tooltip when looking at the anvil screen.")
public class ShowRepairCost extends CharmModule {
    public static List<Component> addRepairCostToTooltip(ItemStack stack, List<Component> tooltip) {
        if (!Charm.LOADER.isEnabled(ShowRepairCost.class)) return tooltip;

        int repairCost = stack.getBaseRepairCost();
        if (repairCost > 0) {
            tooltip.add(TextHelper.empty()); // a new line
            tooltip.add(TextHelper.translatable("gui.charm.repair_cost", repairCost).withStyle(ChatFormatting.GRAY));
        }

        return tooltip;
    }
}
