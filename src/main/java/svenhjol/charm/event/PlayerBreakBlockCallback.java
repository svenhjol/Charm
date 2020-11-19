package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;

public interface PlayerBreakBlockCallback {
    Event<PlayerBreakBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerBreakBlockCallback.class, (listeners) -> (world, gameMode, player, pos) -> {
        for (PlayerBreakBlockCallback listener : listeners) {
            ActionResult result = listener.interact(world, gameMode, player, pos);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(ServerWorld world, GameMode gameMode, PlayerEntity player, BlockPos pos);
}
