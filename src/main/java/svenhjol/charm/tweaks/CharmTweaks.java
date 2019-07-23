package svenhjol.charm.tweaks;

import svenhjol.charm.tweaks.feature.PickaxesBreakPistons;
import svenhjol.charm.tweaks.feature.UseTotemFromInventory;
import svenhjol.meson.Module;

public class CharmTweaks extends Module
{
    public CharmTweaks()
    {
        features.add(new PickaxesBreakPistons());
        features.add(new UseTotemFromInventory());
    }
}
