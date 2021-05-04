package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;

public interface TakeAnvilOutputCallback {
    Event<TakeAnvilOutputCallback> EVENT = EventFactory.createArrayBacked(TakeAnvilOutputCallback.class, (listeners) -> (handler, player, outputStack) -> {
        for (TakeAnvilOutputCallback listener : listeners) {
            listener.interact(handler, player, outputStack);
        }
    });

    void interact(AnvilScreenHandler handler, PlayerEntity player, ItemStack outputStack);
}
