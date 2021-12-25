package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ItemTooltipImageCallback {
    Event<ItemTooltipImageCallback> EVENT = EventFactory.createArrayBacked(ItemTooltipImageCallback.class, (listeners) -> (stack) -> {
        for (ItemTooltipImageCallback listener : listeners) {
            Optional<TooltipComponent> result = listener.interact(stack);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    });

    Optional<TooltipComponent> interact(ItemStack stack);
}
