package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.meson.Feature;
import svenhjol.meson.MesonLoader;

public class CharmLoader extends MesonLoader
{
    public static final MesonLoader INSTANCE = new CharmLoader().register(Charm.MOD_ID);

    public static boolean hasFeature(Class<? extends Feature> feature)
    {
        return MesonLoader.hasFeature(Charm.MOD_ID, feature);
    }
}
