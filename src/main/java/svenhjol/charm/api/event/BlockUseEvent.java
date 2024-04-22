package svenhjol.charm.api.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("unused")
public class BlockUseEvent extends CharmEvent<BlockUseEvent.Handler> {
    public static final BlockUseEvent INSTANCE = new BlockUseEvent();

    private BlockUseEvent() {}

    public InteractionResult invoke(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        for (var handler : getHandlers()) {
            var result = handler.run(player, level, hand, hitResult);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(Player player, Level level, InteractionHand hand, BlockHitResult hitResult);
    }
}
