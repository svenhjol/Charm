package svenhjol.charm.automation.feature;

import svenhjol.charm.automation.block.GunpowderBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.AUTOMATION)
public class BlockOfGunpowder extends Feature
{
    public static GunpowderBlock block;

    @Override
    public void init()
    {
        block = new GunpowderBlock();
    }
}
