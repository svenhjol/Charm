package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.CharmLoader;
import svenhjol.meson.MesonLoader;

@Mod(Charm.MOD_ID)
public class Charm
{
    public static final String MOD_ID = "charm";
    public static MesonLoader loader;

    public Charm()
    {
        loader = new CharmLoader();
    }
}