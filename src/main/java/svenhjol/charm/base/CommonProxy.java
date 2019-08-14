package svenhjol.charm.base;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.automation.CharmAutomation;
import svenhjol.charm.brewing.CharmBrewing;
import svenhjol.charm.crafting.CharmCrafting;
import svenhjol.charm.decoration.CharmDecoration;
import svenhjol.charm.enchanting.CharmEnchanting;
import svenhjol.charm.tweaks.CharmTweaks;
import svenhjol.charm.world.CharmWorld;
import svenhjol.meson.iface.IMesonSidedProxy;

public class CommonProxy implements IMesonSidedProxy
{
    public void init()
    {
        Charm.loader.add(
            new CharmAutomation(),
            new CharmBrewing(),
            new CharmCrafting(),
            new CharmDecoration(),
            new CharmEnchanting(),
            new CharmTweaks(),
            new CharmWorld()
        );

        Charm.loader.bus.addListener(this::setup);
        Charm.loader.bus.addListener(this::loadComplete);
    }

    public void setup(FMLCommonSetupEvent event)
    {
        Charm.loader.setup(event);
    }

    public void loadComplete(FMLLoadCompleteEvent event)
    {
        Charm.loader.loadComplete(event);
    }
}
