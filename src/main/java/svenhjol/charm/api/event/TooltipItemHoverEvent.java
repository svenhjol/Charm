package svenhjol.charm.api.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

@SuppressWarnings("unused")
public class TooltipItemHoverEvent extends CharmEvent<TooltipItemHoverEvent.Handler> {
    public static TooltipItemHoverEvent INSTANCE = new TooltipItemHoverEvent();

    private TooltipItemHoverEvent() {}

    public void invoke(ItemStack stack, List<Component> lines, TooltipFlag tooltipFlag) {
        for (var handler : getHandlers()) {
            handler.run(stack, lines, tooltipFlag);
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(ItemStack stack, List<Component> lines, TooltipFlag tooltipFlag);
    }
}
