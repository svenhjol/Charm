package svenhjol.charm.crafting;

import svenhjol.charm.crafting.feature.EnderPearlBlock;
import svenhjol.charm.crafting.feature.GoldLantern;
import svenhjol.meson.Module;

public class CharmCrafting extends Module
{
    public CharmCrafting()
    {
        features.add(new EnderPearlBlock());
        features.add(new GoldLantern());
    }
}
