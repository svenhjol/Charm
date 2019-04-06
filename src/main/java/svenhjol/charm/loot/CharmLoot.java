package svenhjol.charm.loot;

import svenhjol.meson.Module;
import svenhjol.charm.loot.feature.*;

public class CharmLoot extends Module
{
    public CharmLoot()
    {
        features.add(new AbandonedCrates());
        features.add(new TotemOfReturning());
        features.add(new TotemOfShielding());
        features.add(new UnearthItems());
        features.add(new WitchesDropCorruption());
        features.add(new WitchesDropLuck());
        features.add(new WitchHatProtection());
    }
}