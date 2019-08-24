package svenhjol.charm.building.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.building.block.RottenFleshBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BUILDING)
public class BlockOfRottenFlesh extends MesonModule
{
    public static RottenFleshBlock block;
    public static float hardness = 1.0F;
    public static float resistance = 2.0F;

    @Override
    public void init()
    {
        block = new RottenFleshBlock(this);
    }
}
