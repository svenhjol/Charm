package svenhjol.charm.feature.beacons_heal_mobs.common;

import svenhjol.charm.charmony.event.ApplyBeaconEffectsEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.beacons_heal_mobs.BeaconsHealMobs;

public final class Registers extends RegisterHolder<BeaconsHealMobs> {
    public Registers(BeaconsHealMobs feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        ApplyBeaconEffectsEvent.INSTANCE.handle(feature().handlers::applyBeaconEffects);
    }
}
