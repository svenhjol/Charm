package svenhjol.charm.api.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unused")
public class EntityHurtEvent extends CharmEvent<EntityHurtEvent.Handler> {
    public static final EntityHurtEvent INSTANCE = new EntityHurtEvent();

    private EntityHurtEvent() {}

    public InteractionResult invoke(LivingEntity entity, DamageSource source, float damage) {
        for (Handler handler : getHandlers()) {
            var result = handler.run(entity, source, damage);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(LivingEntity entity, DamageSource source, float damage);
    }
}
