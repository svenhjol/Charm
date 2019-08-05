package svenhjol.charm.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.meson.handler.ModelHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        CharmLoader.INSTANCE.preInitClient(event);

        // this registers all the models for client
		MinecraftForge.EVENT_BUS.register(ModelHandler.class);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        CharmLoader.INSTANCE.initClient(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        CharmLoader.INSTANCE.postInitClient(event);
    }
}