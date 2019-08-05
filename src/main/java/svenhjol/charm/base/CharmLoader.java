package svenhjol.charm.base;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.meson.MesonLoader;

public final class CharmLoader extends MesonLoader
{
    public static final MesonLoader INSTANCE = new CharmLoader();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        CharmSounds.registerSounds();
        CharmLootTables.registerLootTables();
    }
}