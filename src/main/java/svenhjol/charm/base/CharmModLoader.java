package svenhjol.charm.base;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.ModLoader;

import java.io.File;

public final class CharmModLoader extends ModLoader
{
    public static final ModLoader INSTANCE = new CharmModLoader();

    @Override
    protected Configuration setupConfig(FMLPreInitializationEvent event)
    {
        // set up configuration
        File configFile = new File(event.getModConfigurationDirectory(), Charm.MOD_ID + ".cfg");

//        configFile.delete();
        config = new Configuration(configFile, Charm.MOD_VERSION, true);
        config.load();

        CharmSounds.registerSounds();
        CharmLootTables.registerLootTables();

        return config;
    }
}