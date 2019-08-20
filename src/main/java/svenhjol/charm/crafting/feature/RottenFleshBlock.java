package svenhjol.charm.crafting.feature;

import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.crafting.block.RottenFleshBlockBlock;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;


@MesonLoadModule(category = CharmCategories.CRAFTING)
public class RottenFleshBlock extends Feature
{
    public static RottenFleshBlockBlock block;
    public static float hardness = 1.0F;
    public static float resistance = 2.0F;

    @Override
    public void init()
    {
        block = new RottenFleshBlockBlock();
    }
}
