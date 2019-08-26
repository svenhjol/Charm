package svenhjol.charm.tweaks.module;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS)
public class HusksIgnoreSkylight extends MesonModule
{
    public static boolean isSkyLightMax(IWorld world, BlockPos pos)
    {
        return true; // don't care about skylight check for spawning husks
    }
}
