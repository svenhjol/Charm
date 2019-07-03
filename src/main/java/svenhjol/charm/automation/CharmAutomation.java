package svenhjol.charm.automation;

import svenhjol.charm.automation.feature.GunpowderBlock;
import svenhjol.charm.automation.feature.RedstoneSand;
import svenhjol.charm.automation.feature.VariableRedstoneLamp;
import svenhjol.meson.Module;

public class CharmAutomation extends Module
{
    public CharmAutomation()
    {
        features.add(new GunpowderBlock());
        features.add(new RedstoneSand());
        features.add(new VariableRedstoneLamp());
    }
}
