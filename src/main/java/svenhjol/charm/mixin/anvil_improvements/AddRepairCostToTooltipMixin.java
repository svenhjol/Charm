package svenhjol.charm.mixin.anvil_improvements;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.module.anvil_improvements.AnvilImprovements;

import java.util.List;

@Mixin(AnvilScreen.class)
public abstract class AddRepairCostToTooltipMixin extends ForgingScreen<AnvilScreenHandler> {
    public AddRepairCostToTooltipMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    /**
     * Override the default method for getting tooltip and
     * adds the repair cost to it, if enabled.
     */
    @Override
    public List<Text> getTooltipFromItem(ItemStack stack) {
        List<Text> tooltip = super.getTooltipFromItem(stack);
        return AnvilImprovements.addRepairCostToTooltip(stack, tooltip);
    }
}
