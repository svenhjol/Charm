package svenhjol.charm.base;

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
        CharmLoader.INSTANCE.add(
            new CharmAutomation(),
            new CharmBrewing(),
            new CharmCrafting(),
            new CharmDecoration(),
            new CharmEnchanting(),
            new CharmTweaks(),
            new CharmWorld()
        );

        CharmLoader.getEventBus().addListener(CharmLoader::setup);
    }
}
