package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;

public interface CheckAnvilRepairCallback {
    Event<CheckAnvilRepairCallback> EVENT = EventFactory.createArrayBacked(CheckAnvilRepairCallback.class, (listeners) -> (handler, player, leftStack, rightStack) -> {
        for (CheckAnvilRepairCallback listener : listeners) {
            if (listener.interact(handler, player, leftStack, rightStack))
                return true;
        }

        return false;
    });

    boolean interact(AnvilScreenHandler handler, PlayerEntity player, ItemStack leftStack, ItemStack rightStack);
}
