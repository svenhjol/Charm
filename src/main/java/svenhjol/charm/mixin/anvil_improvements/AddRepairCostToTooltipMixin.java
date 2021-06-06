package svenhjol.charm.mixin.anvil_improvements;

import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.module.anvil_improvements.AnvilImprovements;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

@Mixin(AnvilScreen.class)
public abstract class AddRepairCostToTooltipMixin extends ItemCombinerScreen<AnvilMenu> {
    public AddRepairCostToTooltipMixin(AnvilMenu handler, Inventory playerInventory, Component title, ResourceLocation texture) {
        super(handler, playerInventory, title, texture);
    }

    /**
     * Override the default method for getting tooltip and
     * adds the repair cost to it, if enabled.
     */
    @Override
    public List<Component> getTooltipFromItem(ItemStack stack) {
        List<Component> tooltip = super.getTooltipFromItem(stack);
        return AnvilImprovements.addRepairCostToTooltip(stack, tooltip);
    }
}
