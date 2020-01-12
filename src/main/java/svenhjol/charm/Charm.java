package svenhjol.charm;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.CharmLoader;
import svenhjol.meson.MesonModule;

@Mod(Charm.MOD_ID)
public class Charm
{
    public static final String MOD_ID = "charm";

    public Charm()
    {
        new CharmLoader();
    }

    public static boolean hasModule(String module)
    {
        return CharmLoader.hasModule(new ResourceLocation(MOD_ID, module));
    }

    public static boolean hasModule(Class<? extends MesonModule> module)
    {
        return hasModule(module.getSimpleName().toLowerCase());
    }
}