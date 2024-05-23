package svenhjol.charm.feature.animal_damage_immunity.common;

import svenhjol.charm.api.event.EntityAttackEvent;
import svenhjol.charm.api.event.EntityHurtEvent;
import svenhjol.charm.feature.animal_damage_immunity.AnimalDamageImmunity;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<AnimalDamageImmunity> {
    public Registers(AnimalDamageImmunity feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        EntityAttackEvent.INSTANCE.handle(feature().handlers::entityAttack);
        EntityHurtEvent.INSTANCE.handle(feature().handlers::entityHurt);
    }
}