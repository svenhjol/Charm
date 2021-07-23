package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public interface CheckAnvilRepairEvent {
    Event<CheckAnvilRepairEvent> EVENT = EventFactory.createArrayBacked(CheckAnvilRepairEvent.class, (listeners) -> (handler, player, leftStack, rightStack) -> {
        for (CheckAnvilRepairEvent listener : listeners) {
            boolean result = listener.interact(handler, player, leftStack, rightStack);
            if (result)
                return true;
        }

        return false;
    });

    boolean interact(AnvilMenu handler, Player player, ItemStack leftStack, ItemStack rightStack);
}
