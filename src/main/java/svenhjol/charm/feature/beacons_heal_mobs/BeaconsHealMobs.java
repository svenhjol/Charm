package svenhjol.charm.feature.beacons_heal_mobs;

import svenhjol.charm.feature.beacons_heal_mobs.common.Advancements;
import svenhjol.charm.feature.beacons_heal_mobs.common.Handlers;
import svenhjol.charm.feature.beacons_heal_mobs.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Passive and friendly mobs will heal themselves within range of a beacon with the regeneration effect.")
public final class BeaconsHealMobs extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public BeaconsHealMobs(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
     }
}
