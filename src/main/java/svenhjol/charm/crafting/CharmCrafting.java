package svenhjol.charm.crafting;

import svenhjol.charm.crafting.feature.EnderPearlBlock;
import svenhjol.meson.Module;

public class CharmCrafting extends Module
{
    public CharmCrafting()
    {
        features.add(new EnderPearlBlock());
    }
}
