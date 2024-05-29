package svenhjol.charm.charmony.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * A custom Fabric event that fires on every tooltip render.
 */
public interface RenderTooltipCallback {
    Event<RenderTooltipCallback> EVENT = EventFactory.createArrayBacked(RenderTooltipCallback.class, (listeners) -> (guiGraphics, stack, lines, x, y) -> {
        for (RenderTooltipCallback listener : listeners) {
            listener.interact(guiGraphics, stack, lines, x, y);
        }
    });

    void interact(GuiGraphics guiGraphics, ItemStack stack, List<ClientTooltipComponent> lines, int x, int y);
}
