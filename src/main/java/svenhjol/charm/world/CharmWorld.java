package svenhjol.charm.world;

import svenhjol.charm.world.feature.CompassBinding;
import svenhjol.charm.world.feature.NetherGoldDeposits;
import svenhjol.charm.world.feature.StructureMaps;
import svenhjol.meson.Module;

public class CharmWorld extends Module
{
    public CharmWorld()
    {
        features.add(new CompassBinding());
        features.add(new NetherGoldDeposits());
        features.add(new StructureMaps());
    }
}
