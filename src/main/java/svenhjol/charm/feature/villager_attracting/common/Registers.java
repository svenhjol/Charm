package svenhjol.charm.feature.villager_attracting.common;

import svenhjol.charm.charmony.event.EntityJoinEvent;
import svenhjol.charm.charmony.event.PlayerTickEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.villager_attracting.VillagerAttracting;

public final class Registers extends RegisterHolder<VillagerAttracting> {
    public Registers(VillagerAttracting feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        EntityJoinEvent.INSTANCE.handle(feature().handlers::entityJoin);
        PlayerTickEvent.INSTANCE.handle(feature().handlers::playerTick);
    }
}
