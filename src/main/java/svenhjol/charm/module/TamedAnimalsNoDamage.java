package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import svenhjol.meson.event.HurtEntityCallback;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Tamed animals do not take direct damage from players.")
public class TamedAnimalsNoDamage extends MesonModule {
    @Override
    public void init() {
        AttackEntityCallback.EVENT.register(this::tryIgnoreAttack);
        HurtEntityCallback.EVENT.register(this::tryIgnoreDamage);
    }

    private ActionResult tryIgnoreAttack(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (entity instanceof TameableEntity
            && ((TameableEntity)entity).isTamed()
            && !player.isCreative()
        ) {
            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    private ActionResult tryIgnoreDamage(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!(entity instanceof PlayerEntity)) {
            Entity attacker = damageSource.getAttacker();
            Entity source = damageSource.getSource();

            PlayerEntity player = null;

            if (source instanceof PlayerEntity) player = (PlayerEntity) source;
            if (attacker instanceof PlayerEntity) player = (PlayerEntity) attacker;

            if (player != null && !player.isCreative())
                if (entity instanceof TameableEntity && ((TameableEntity) entity).isTamed())
                    return ActionResult.FAIL; // the positive outcome!
        }

        return ActionResult.PASS;
    }
}
