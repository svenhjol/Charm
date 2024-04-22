package svenhjol.charm.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public class BlockBreakSpeedEvent extends CharmEvent<BlockBreakSpeedEvent.Handler> {
    public static final BlockBreakSpeedEvent INSTANCE = new BlockBreakSpeedEvent();
    
    private BlockBreakSpeedEvent() {}

    public float invoke(Player player, BlockState state, float currentSpeed) {
        float speed = currentSpeed;

        for (var handler : BlockBreakSpeedEvent.INSTANCE.getHandlers()) {
            speed = handler.run(player, state, speed);
        }

        return speed;
    }
    
    @FunctionalInterface
    public interface Handler {
        float run(Player player, BlockState state, float currentSpeed);
    }
}