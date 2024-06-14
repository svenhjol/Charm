package svenhjol.charm.feature.animal_damage_immunity;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.animal_damage_immunity.common.Advancements;
import svenhjol.charm.feature.animal_damage_immunity.common.Handlers;

@Feature(description = "Tamed animals do not take direct damage from players.")
public final class AnimalDamageImmunity extends CommonFeature {
    public final Handlers handlers;
    public final Advancements advancements;

    public AnimalDamageImmunity(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
