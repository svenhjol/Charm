package svenhjol.charm.feature.animal_damage_immunity.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.animal_damage_immunity.AnimalDamageImmunity;

public final class Handlers extends FeatureHolder<AnimalDamageImmunity> {
    public Handlers(AnimalDamageImmunity feature) {
        super(feature);
    }

    public boolean shouldHurt(LivingEntity entity, DamageSource damageSource) {
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
                if (player instanceof ServerPlayer serverPlayer) {
                    feature().advancements.negatedAnimalDamage(serverPlayer);
                }
                return false;
            }
        }

        return true;
    }

    public boolean isPet(Entity entity) {
        return (entity instanceof TamableAnimal && ((TamableAnimal) entity).isTame())
            || (entity instanceof OwnableEntity && ((OwnableEntity) entity).getOwnerUUID() != null);
    }
}
