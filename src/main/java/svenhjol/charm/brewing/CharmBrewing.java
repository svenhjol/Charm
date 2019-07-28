package svenhjol.charm.brewing;

import svenhjol.charm.brewing.feature.Coffee;
import svenhjol.charm.brewing.feature.Decay;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.meson.Module;

public class CharmBrewing extends Module
{
    public CharmBrewing()
    {
        features.add(new Coffee());
        features.add(new Decay());
        features.add(new FlavoredCake());
    }
}
