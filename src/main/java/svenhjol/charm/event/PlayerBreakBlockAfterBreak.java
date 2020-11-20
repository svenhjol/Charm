package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

/**
 * This is a highly specific event for hooking into ServerPlayerInteractionManager's
 * tryBreakBlock at the point at which the block is removed but hasn't dropped anything yet.
 */
public interface PlayerBreakBlockAfterBreak {
    Event<PlayerBreakBlockAfterBreak> EVENT = EventFactory.createArrayBacked(PlayerBreakBlockAfterBreak.class, (listeners) -> (world, player, pos, state, blockEntity) -> {
        for (PlayerBreakBlockAfterBreak listener : listeners) {
            ActionResult result = listener.interact(world, player, pos, state, blockEntity);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(ServerWorld world, PlayerEntity playerEntity, BlockPos pos, BlockState state, BlockEntity blockEntity);
}
