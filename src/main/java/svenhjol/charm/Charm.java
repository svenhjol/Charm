package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import svenhjol.charm.base.CharmModLoader;
import svenhjol.charm.base.CommonProxy;
import svenhjol.charm.brewing.CharmBrewing;
import svenhjol.charm.crafting.CharmCrafting;
import svenhjol.charm.enchanting.CharmEnchanting;
import svenhjol.charm.loot.CharmLoot;
import svenhjol.charm.tweaks.CharmTweaks;
import svenhjol.charm.smithing.CharmSmithing;
import svenhjol.charm.world.CharmWorld;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.Module;

@SuppressWarnings("unused")
@Mod(modid = Charm.MOD_ID, version = Charm.MOD_VERSION, name = Charm.MOD_NAME)
public class Charm
{
    public static final String MOD_NAME = "Charm";
    public static final String MOD_ID = "charm";
    public static final String MOD_VERSION = "@MOD_VERSION@";
    public static final String PROXY_COMMON = "svenhjol.charm.base.CommonProxy";
	public static final String PROXY_CLIENT = "svenhjol.charm.base.ClientProxy";

    @Instance(Charm.MOD_ID)
    public static Charm instance;

    @SidedProxy(serverSide = Charm.PROXY_COMMON, clientSide = Charm.PROXY_CLIENT)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // set up the Meson framework
        Meson.init();

        // add modules to the Charm mod loader
        CharmModLoader.INSTANCE.registerModLoader(Charm.MOD_ID).add(
            new CharmBrewing(),
            new CharmCrafting(),
            new CharmEnchanting(),
            new CharmLoot(),
            new CharmTweaks(),
            new CharmSmithing(),
            new CharmWorld()
        );

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        proxy.finalInit(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        proxy.serverStarting(event);
    }

    public static boolean hasFeature(Class<? extends Feature> feature)
    {
        return CharmModLoader.INSTANCE.enabledFeatures.contains(feature);
    }

    public static boolean hasModule(Class<? extends Module> module)
    {
        return CharmModLoader.INSTANCE.enabledModules.contains(module);
    }
}