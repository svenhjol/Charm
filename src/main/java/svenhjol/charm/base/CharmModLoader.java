package svenhjol.charm.base;

import svenhjol.meson.ModLoader;

public final class CharmModLoader extends ModLoader
{
    public static final ModLoader INSTANCE = new CharmModLoader();

    @Override
    protected void setupConfig()
    {
        CharmSounds.registerSounds();
    }
}