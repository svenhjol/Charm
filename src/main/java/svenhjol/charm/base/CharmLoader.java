package svenhjol.charm.base;

import svenhjol.meson.Feature;
import svenhjol.meson.MesonLoader;

public final class CharmLoader extends MesonLoader
{
    public static final MesonLoader INSTANCE = new CharmLoader();

    public static boolean hasFeature(Class<? extends Feature> feature)
    {
        return CharmLoader.INSTANCE.enabledFeatures.containsKey(feature);
    }
}
