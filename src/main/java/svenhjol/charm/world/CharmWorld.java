package svenhjol.charm.world;

import svenhjol.charm.world.feature.NetherGoldDeposits;
import svenhjol.charm.world.feature.TotemOfReturning;
import svenhjol.meson.Module;

public class CharmWorld extends Module
{
    public CharmWorld()
    {
        features.add(new NetherGoldDeposits());
        features.add(new TotemOfReturning());
    }
}
