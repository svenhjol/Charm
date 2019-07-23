package svenhjol.charm.tools;

import svenhjol.charm.tools.feature.TotemOfReturning;
import svenhjol.meson.Module;

public class CharmTools extends Module
{
    public CharmTools()
    {
        features.add(new TotemOfReturning());
    }
}
