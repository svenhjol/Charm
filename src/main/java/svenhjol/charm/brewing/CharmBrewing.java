package svenhjol.charm.brewing;

import svenhjol.meson.Module;
import svenhjol.charm.brewing.feature.*;

public class CharmBrewing extends Module
{
    public CharmBrewing()
    {
        features.add(new Coffee());
        features.add(new Decay());
        features.add(new EnderSight());
        features.add(new FlavoredCake());
        features.add(new PoisonousPotatoBrew());
    }
}