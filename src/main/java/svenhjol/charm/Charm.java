package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.CharmLoader;
import svenhjol.meson.MesonLoader;
import svenhjol.meson.MesonModule;

@Mod(Charm.MOD_ID)
public class Charm
{
    public static final String MOD_ID = "charm";
    public static MesonLoader loader = null;

    public Charm()
    {
        loader = new CharmLoader();
    }

    public static boolean hasModule(String module)
    {
        return loader != null && loader.hasModule(module);
    }


    public static boolean hasModule(Class<? extends MesonModule> module)
    {
        return loader != null && loader.hasModule(module);
    }
}