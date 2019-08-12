package svenhjol.charm.enchanting;

import svenhjol.charm.enchanting.feature.Magnetic;
import svenhjol.meson.Module;

public class CharmEnchanting extends Module
{
    public CharmEnchanting()
    {
        features.add(new Magnetic());
    }
}
