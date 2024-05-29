package svenhjol.charm.feature.villager_attracting.common;

import svenhjol.charm.api.event.EntityJoinEvent;
import svenhjol.charm.api.event.PlayerTickEvent;
import svenhjol.charm.feature.villager_attracting.VillagerAttracting;
import svenhjol.charm.charmony.feature.RegisterHolder;

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
