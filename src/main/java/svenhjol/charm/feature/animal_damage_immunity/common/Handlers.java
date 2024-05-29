package svenhjol.charm.feature.animal_damage_immunity.common;

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
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.animal_damage_immunity.AnimalDamageImmunity;

public final class Handlers extends FeatureHolder<AnimalDamageImmunity> {
    public Handlers(AnimalDamageImmunity feature) {
        super(feature);
    }

    public InteractionResult entityHurt(LivingEntity entity, DamageSource damageSource, float damage) {
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

    public InteractionResult entityAttack(Player player, Level level, InteractionHand hand, Entity target, EntityHitResult hitResult) {
        if (!player.getAbilities().instabuild && isPet(target)) {
            return InteractionResult.FAIL; // the positive outcome!
        }

        return InteractionResult.PASS;
    }

    public boolean isPet(Entity entity) {
        return (entity instanceof TamableAnimal && ((TamableAnimal) entity).isTame())
            || (entity instanceof OwnableEntity && ((OwnableEntity) entity).getOwnerUUID() != null);
    }
}
