package svenhjol.charm.api.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class EntityUseEvent extends CharmEvent<EntityUseEvent.Handler> {
    public static final EntityUseEvent INSTANCE = new EntityUseEvent();

    private EntityUseEvent() {}

    public InteractionResult invoke(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        for (var handler : getHandlers()) {
            var result = handler.run(player, level, hand, entity, hitResult);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult);
    }
}
