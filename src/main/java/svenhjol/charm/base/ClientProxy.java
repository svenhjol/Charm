package svenhjol.charm.base;

import svenhjol.meson.MesonLoader;
import svenhjol.meson.iface.IMesonSidedProxy;

public class ClientProxy extends CommonProxy implements IMesonSidedProxy
{
    @Override
    public void init()
    {
        super.init();

        CharmSounds.init();

        MesonLoader.getEventBus().addListener(MesonLoader::setupClient);
    }
}
