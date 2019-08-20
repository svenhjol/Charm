package svenhjol.charm.automation.feature;

import svenhjol.charm.automation.block.RedstoneSandBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.AUTOMATION)
public class RedstoneSand extends Feature
{
    public static RedstoneSandBlock block;

    @Override
    public void init()
    {
        block = new RedstoneSandBlock();
    }
}
