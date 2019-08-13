package svenhjol.charm.base;

import svenhjol.meson.iface.IMesonSidedProxy;

public class ClientProxy extends CommonProxy implements IMesonSidedProxy
{
    @Override
    public void init()
    {
        super.init();

        CharmSounds.init();

        CharmLoader.getEventBus().addListener(CharmLoader::setupClient);
    }
}
