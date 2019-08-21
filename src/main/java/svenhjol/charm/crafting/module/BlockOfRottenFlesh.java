package svenhjol.charm.crafting.module;

import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.crafting.block.RottenFleshBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;


@Module(category = CharmCategories.CRAFTING)
public class BlockOfRottenFlesh extends MesonModule
{
    public static RottenFleshBlock block;
    public static float hardness = 1.0F;
    public static float resistance = 2.0F;

    @Override
    public void init()
    {
        block = new RottenFleshBlock();
    }
}
