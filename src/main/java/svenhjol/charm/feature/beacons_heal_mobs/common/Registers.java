package svenhjol.charm.feature.beacons_heal_mobs.common;

import svenhjol.charm.api.event.ApplyBeaconEffectsEvent;
import svenhjol.charm.feature.beacons_heal_mobs.BeaconsHealMobs;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<BeaconsHealMobs> {
    public Registers(BeaconsHealMobs feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ApplyBeaconEffectsEvent.INSTANCE.handle(feature().handlers::applyBeaconEffects);
    }
}
