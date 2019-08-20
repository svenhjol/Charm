package svenhjol.charm.decoration.feature;

import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.GoldLanternBlock;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.DECORATION)
public class GoldLantern extends Feature
{
    public static GoldLanternBlock block;

    @Override
    public void init()
    {
        block = new GoldLanternBlock();
    }
}
