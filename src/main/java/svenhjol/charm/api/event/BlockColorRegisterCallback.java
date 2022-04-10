package svenhjol.charm.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.color.block.BlockColors;

/**
 * Allows clients to register custom colored blocks.
 */
public interface BlockColorRegisterCallback {
    Event<BlockColorRegisterCallback> EVENT = EventFactory.createArrayBacked(BlockColorRegisterCallback.class, listeners -> blockColors -> {
        for (BlockColorRegisterCallback listener : listeners) {
            listener.interact(blockColors);
        }
    });

    void interact(BlockColors blockColors);
}
