package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface StackItemOnItemCallback {
    Event<StackItemOnItemCallback> EVENT = EventFactory.createArrayBacked(StackItemOnItemCallback.class, (listeners) -> (direction, source, dest, slot, clickAction, player, slotAccess) -> {
        for (StackItemOnItemCallback listener : listeners) {
            boolean result = listener.interact(direction, source, dest, slot, clickAction, player, slotAccess);
            if (result)
                return true;
        }

        return false;
    });

    boolean interact(Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess);

    enum Direction {
        STACKED_ON_OTHER,
        STACKED_ON_SELF
    }
}
