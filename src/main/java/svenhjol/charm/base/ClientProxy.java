package svenhjol.charm.base;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonSidedProxy;

public class ClientProxy extends CommonProxy implements IMesonSidedProxy
{
    @Override
    public void init()
    {
        super.init();

        Charm.loader.bus.addListener(this::setupClient);
    }

    public void setupClient(FMLClientSetupEvent event)
    {
        Charm.loader.setupClient(event);
    }
}
