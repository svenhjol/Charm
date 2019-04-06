package svenhjol.charm.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.meson.ModelHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        CharmModLoader.INSTANCE.preInitClient(event);

        // this registers all the models for client
		MinecraftForge.EVENT_BUS.register(ModelHandler.class);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        CharmModLoader.INSTANCE.initClient(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        CharmModLoader.INSTANCE.postInitClient(event);
    }
}