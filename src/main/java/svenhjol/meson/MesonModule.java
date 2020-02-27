package svenhjol.meson;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public abstract class MesonModule
{
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean configureEnabled = true;
    public boolean hasSubscriptions = false;
    public String mod = "";
    public String category = "";
    public String name = "";
    public String description = "";

    public boolean shouldBeEnabled()
    {
        return true;
    }

    public String getName()
    {
        return this.name.isEmpty() ? this.getClass().getSimpleName() : this.name;
    }

    public void init()
    {
        // register blocks, TEs, etc
    }

    @OnlyIn(Dist.CLIENT)
    public void initClient()
    {
        // register particles and other client side things
    }

    public void onCommonSetup(FMLCommonSetupEvent event)
    {
        // register messages, composter items, etc
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event)
    {
        // register screens, etc
    }

    public void onModConfig(ModConfigEvent event)
    {
        // modules can be enabled/disabled when config changes
    }

    public void onServerAboutToStart(FMLServerAboutToStartEvent event)
    {
        // server-side event, just after play selected world clicked
    }

    public void onServerStarting(FMLServerStartingEvent event)
    {
        // server-side event, access to resource manager etc
    }

    public void onServerStarted(FMLServerStartedEvent event)
    {
        // server-side event, access to resource manager etc
    }

    public void onLoadComplete(FMLLoadCompleteEvent event)
    {
        // do final things
    }
}
