package svenhjol.charm.decoration;

import svenhjol.charm.decoration.feature.AllTheBarrels;
import svenhjol.charm.decoration.feature.GoldLantern;
import svenhjol.charm.decoration.feature.RandomAnimalTextures;
import svenhjol.meson.Module;

public class CharmDecoration extends Module
{
    public CharmDecoration()
    {
        features.add(new AllTheBarrels());
        features.add(new GoldLantern());
        features.add(new RandomAnimalTextures());
    }
}
