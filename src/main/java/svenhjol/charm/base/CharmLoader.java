package svenhjol.charm.base;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonLoader;

import java.io.File;

public final class CharmLoader extends MesonLoader
{
    public static final MesonLoader INSTANCE = new CharmLoader();

    @Override
    protected Configuration setupConfig(FMLPreInitializationEvent event)
    {
        // set up configuration
        File configFile = new File(event.getModConfigurationDirectory(), Charm.MOD_ID + ".cfg");

        // debug mode
        File debugFile = new File(event.getModConfigurationDirectory(), Charm.MOD_ID + ".debug.cfg");
        Meson.DEBUG = debugFile.exists();

        config = new Configuration(configFile, Charm.MOD_VERSION, true);
        config.load();

        CharmSounds.registerSounds();

        return config;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        CharmLootTables.registerLootTables();
        super.preInit(event);
    }
}