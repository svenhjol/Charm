package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public interface TakeAnvilOutputEvent {
    Event<TakeAnvilOutputEvent> EVENT = EventFactory.createArrayBacked(TakeAnvilOutputEvent.class, (listeners) -> (handler, player, outputStack) -> {
        for (TakeAnvilOutputEvent listener : listeners) {
            listener.interact(handler, player, outputStack);
        }
    });

    void interact(AnvilMenu handler, Player player, ItemStack outputStack);
}
