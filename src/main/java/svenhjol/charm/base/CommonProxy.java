package svenhjol.charm.base;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import svenhjol.charm.Charm;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        // register the Charm GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(Charm.instance, new GuiHandler());

        // configure and initialise the mod loader
        CharmModLoader.INSTANCE.preInit(event);
    }

    public void init(FMLInitializationEvent event)
    {
        CharmModLoader.INSTANCE.init(event);
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        CharmModLoader.INSTANCE.postInit(event);
    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        CharmModLoader.INSTANCE.serverStarting(event);
    }
}