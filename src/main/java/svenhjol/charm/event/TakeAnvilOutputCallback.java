package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public interface TakeAnvilOutputCallback {
    Event<TakeAnvilOutputCallback> EVENT = EventFactory.createArrayBacked(TakeAnvilOutputCallback.class, (listeners) -> (handler, player, outputStack) -> {
        for (TakeAnvilOutputCallback listener : listeners) {
            listener.interact(handler, player, outputStack);
        }
    });

    void interact(AnvilMenu handler, Player player, ItemStack outputStack);
}
