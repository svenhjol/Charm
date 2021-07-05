package svenhjol.charm.module.tamed_animals_no_damage;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.event.EntityHurtCallback;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Tamed animals do not take direct damage from players.")
public class TamedAnimalsNoDamage extends CharmModule {
    @Override
    public void runWhenEnabled() {
        AttackEntityCallback.EVENT.register(this::tryIgnoreAttack);
        EntityHurtCallback.EVENT.register(this::tryIgnoreDamage);
    }

    private InteractionResult tryIgnoreAttack(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (entity instanceof TamableAnimal
            && ((TamableAnimal)entity).isTame()
            && !player.isCreative()
        ) {
            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    private InteractionResult tryIgnoreDamage(LivingEntity entity, DamageSource damageSource, float amount) {
        if (!(entity instanceof Player)) {
            Entity attacker = damageSource.getEntity();
            Entity source = damageSource.getDirectEntity();

            Player player = null;

            if (source instanceof Player) player = (Player) source;
            if (attacker instanceof Player) player = (Player) attacker;

            if (player != null && !player.isCreative())
                if (entity instanceof TamableAnimal && ((TamableAnimal) entity).isTame())
                    return InteractionResult.FAIL; // the positive outcome!
        }

        return InteractionResult.PASS;
    }
}
