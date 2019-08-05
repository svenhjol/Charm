package svenhjol.meson.helper;

import net.minecraftforge.fml.common.Loader;

public class ForgeHelper
{
    public static boolean areModsLoaded(String ...mods)
    {
        boolean modsLoaded = true;
        for (String mod : mods) {
            modsLoaded = modsLoaded && Loader.isModLoaded(mod);
        }
        return modsLoaded;
    }
}
