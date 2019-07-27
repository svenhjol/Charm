package svenhjol.charm.brewing;

import svenhjol.charm.brewing.feature.Coffee;
import svenhjol.meson.Module;

public class CharmBrewing extends Module
{
    public CharmBrewing()
    {
        features.add(new Coffee());
    }
}
