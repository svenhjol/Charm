package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import svenhjol.charm.event.HurtEntityCallback;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Tamed animals do not take direct damage from players.")
public class TamedAnimalsNoDamage extends MesonModule {
    @Override
    public void init() {
        AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
            if (entity instanceof TameableEntity
                && ((TameableEntity)entity).isTamed()
                && !player.isCreative()
            ) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        }));

        HurtEntityCallback.EVENT.register(((entity, source, amount) -> {
            boolean result = tryIgnoreDamage(entity, source);
            return result ? ActionResult.FAIL : ActionResult.PASS;
        }));
    }

    private boolean tryIgnoreDamage(LivingEntity entity, DamageSource damageSource) {
        if (!(entity instanceof PlayerEntity)) {
            Entity attacker = damageSource.getAttacker();
            Entity source = damageSource.getSource();

            PlayerEntity player = null;

            if (source instanceof PlayerEntity) player = (PlayerEntity) source;
            if (attacker instanceof PlayerEntity) player = (PlayerEntity) attacker;

            if (player != null && !player.isCreative())
                return (entity instanceof TameableEntity && ((TameableEntity) entity).isTamed());
        }
        return false;
    }
}
