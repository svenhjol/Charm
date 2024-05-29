package svenhjol.charm.feature.animal_damage_immunity;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.animal_damage_immunity.common.Handlers;
import svenhjol.charm.feature.animal_damage_immunity.common.Registers;

@Feature(description = "Tamed animals do not take direct damage from players.")
public final class AnimalDamageImmunity extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public AnimalDamageImmunity(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
