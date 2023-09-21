package svenhjol.charm.feature.no_pet_damage;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmapi.event.EntityAttackEvent;
import svenhjol.charmapi.event.EntityHurtEvent;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Tamed animals do not take direct damage from players.")
public class NoPetDamage extends CharmFeature {
    @Override
    public void runWhenEnabled() {
        EntityAttackEvent.INSTANCE.handle(this::handleEntityAttack);
        EntityHurtEvent.INSTANCE.handle(this::handleEntityHurt);
    }

    private InteractionResult handleEntityHurt(LivingEntity entity, DamageSource damageSource, float damage) {
        if (!(entity instanceof Player)) {
            var attacker = damageSource.getEntity();
            var source = damageSource.getDirectEntity();

            Player player = null;

            if (source instanceof Player) {
                player = (Player) source;
            }
            if (attacker instanceof Player) {
                player = (Player) attacker;
            }

            if (player != null && !player.getAbilities().instabuild && isPet(entity)) {
                return InteractionResult.FAIL; // the positive outcome!
            }
        }

        return InteractionResult.PASS;
    }

    private InteractionResult handleEntityAttack(Player player, Level level, InteractionHand hand, Entity target, EntityHitResult hitResult) {
        if (!player.getAbilities().instabuild && isPet(target)) {
            return InteractionResult.FAIL; // the positive outcome!
        }

        return InteractionResult.PASS;
    }

    private boolean isPet(Entity entity) {
        return (entity instanceof TamableAnimal && ((TamableAnimal) entity).isTame())
            || (entity instanceof OwnableEntity && ((OwnableEntity) entity).getOwnerUUID() != null);
    }
}
