package svenhjol.charm.building.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.building.block.RottenFleshBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BUILDING,
    description = "A storage block for rotten flesh.  If the rotten flesh block has water on any of its sides, it has a chance to turn into dirt.\n" +
        "If there is a soil block above the rotten flesh block, it has a chance to turn into podzol.")
public class BlockOfRottenFlesh extends MesonModule
{
    public static RottenFleshBlock block;

    @Override
    public void init()
    {
        block = new RottenFleshBlock(this);
    }
}
