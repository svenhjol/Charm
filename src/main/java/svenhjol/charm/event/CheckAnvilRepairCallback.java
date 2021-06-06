package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public interface CheckAnvilRepairCallback {
    Event<CheckAnvilRepairCallback> EVENT = EventFactory.createArrayBacked(CheckAnvilRepairCallback.class, (listeners) -> (handler, player, leftStack, rightStack) -> {
        boolean result = true;

        for (CheckAnvilRepairCallback listener : listeners) {
            result = result && listener.interact(handler, player, leftStack, rightStack);
        }

        return result;
    });

    boolean interact(AnvilMenu handler, Player player, ItemStack leftStack, ItemStack rightStack);
}
