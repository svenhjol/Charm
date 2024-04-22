package svenhjol.charm.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class BlockBreakEvent extends CharmEvent<BlockBreakEvent.Handler> {
    public static final BlockBreakEvent INSTANCE = new BlockBreakEvent();

    public boolean invoke(Level level, BlockPos pos, BlockState state, @Nullable Player player) {
        for (var handler : BlockBreakEvent.INSTANCE.getHandlers()) {
            var result = handler.run(level, pos, state, player);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    public interface Handler {
        boolean run(Level level, BlockPos pos, BlockState state, @Nullable Player player);
    }
}
