package svenhjol.charm.crafting;

import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.crafting.feature.EnderPearlBlock;
import svenhjol.meson.Module;

public class CharmCrafting extends Module
{
    public CharmCrafting()
    {
        features.add(new Crate());
        features.add(new EnderPearlBlock());
    }
}
