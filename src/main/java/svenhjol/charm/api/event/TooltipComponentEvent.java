package svenhjol.charm.api.event;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@SuppressWarnings("unused")
public class TooltipComponentEvent extends CharmEvent<TooltipComponentEvent.Handler> {
    public static TooltipComponentEvent INSTANCE = new TooltipComponentEvent();

    private TooltipComponentEvent() {}

    public Optional<TooltipComponent> invoke(ItemStack itemStack) {
        for (var handler : getHandlers()) {
            var result = handler.run(itemStack);

            // Return the first handler that satisfies the itemstack hover.
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    @FunctionalInterface
    public interface Handler {
        Optional<TooltipComponent> run(ItemStack stack);
    }
}
