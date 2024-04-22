package svenhjol.charm.api.event;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unused")
public class TooltipRenderEvent extends CharmEvent<TooltipRenderEvent.Handler> {
    public static final TooltipRenderEvent INSTANCE = new TooltipRenderEvent();

    private TooltipRenderEvent() {}

    public void invoke(GuiGraphics guiGraphics, List<ClientTooltipComponent> lines, int x, int y, @Nullable ItemStack stack) {
        for (var handler : getHandlers()) {
            handler.run(guiGraphics, lines, x, y, stack);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(GuiGraphics guiGraphics, List<ClientTooltipComponent> lines, int x, int y, @Nullable ItemStack stack);
    }
}
