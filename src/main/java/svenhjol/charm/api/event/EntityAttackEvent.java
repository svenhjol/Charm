package svenhjol.charm.api.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class EntityAttackEvent extends CharmEvent<EntityAttackEvent.Handler> {
    public static final EntityAttackEvent INSTANCE = new EntityAttackEvent();

    private EntityAttackEvent() {}

    public InteractionResult invoke(Player attacker, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult hitResult) {
        for (var handler : getHandlers()) {
            var result = handler.run(attacker, level, hand, target, hitResult);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(Player attacker, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult hitResult);
    }
}
