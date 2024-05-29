package svenhjol.charm.charmony.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A custom Fabric event to change the speed of block breaking.
 */
public interface BlockBreakSpeedCallback {
    Event<BlockBreakSpeedCallback> EVENT = EventFactory.createArrayBacked(BlockBreakSpeedCallback.class, 
        listeners -> ((player, state, currentSpeed) -> {
            float speed = currentSpeed;
            
            for (BlockBreakSpeedCallback listener : listeners) {
                speed = listener.interact(player, state, speed);
            }
            
            return speed;
        }));
    
    float interact(Player player, BlockState state, float currentSpeed);
}