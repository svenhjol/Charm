package svenhjol.charm.base;

import svenhjol.meson.MesonLoader;

public final class CharmLoader extends MesonLoader
{
    public static final MesonLoader INSTANCE = new CharmLoader();

    @Override
    public void init()
    {
        super.init();

        CharmSounds.init();
    }
}
