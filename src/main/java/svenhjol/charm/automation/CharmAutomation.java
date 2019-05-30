package svenhjol.charm.automation;

import svenhjol.charm.automation.feature.GunpowderBlock;
import svenhjol.charm.automation.feature.RedstoneSand;
import svenhjol.meson.Module;

public class CharmAutomation extends Module
{
    public CharmAutomation()
    {
        features.add(new GunpowderBlock());
        features.add(new RedstoneSand());
    }
}
