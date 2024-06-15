package svenhjol.charm.feature.animal_reviving.common;

import svenhjol.charm.charmony.event.EntityKilledEvent;
import svenhjol.charm.charmony.event.ItemUseEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.animal_reviving.AnimalReviving;

public final class Registers extends RegisterHolder<AnimalReviving> {
    public Registers(AnimalReviving feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        EntityKilledEvent.INSTANCE.handle(feature().handlers::entityKilled);
        ItemUseEvent.INSTANCE.handle(feature().handlers::itemUsed);
    }
}
