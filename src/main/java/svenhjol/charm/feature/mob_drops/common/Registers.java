package svenhjol.charm.feature.mob_drops.common;

import svenhjol.charm.charmony.event.EntityKilledDropEvent;
import svenhjol.charm.charmony.event.EntityTickEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.mob_drops.MobDrops;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<MobDrops> {
    public final List<DropHandler> dropHandlers = new ArrayList<>();

    public Registers(MobDrops feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        EntityKilledDropEvent.INSTANCE.handle(feature().handlers::entityKilledDrop);
        EntityTickEvent.INSTANCE.handle(feature().handlers::entityTick);
    }
}
