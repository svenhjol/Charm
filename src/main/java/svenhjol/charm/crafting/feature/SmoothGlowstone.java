package svenhjol.charm.crafting.feature;

import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.crafting.block.SmoothGlowstoneBlock;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.CRAFTING)
public class SmoothGlowstone extends Feature
{
    public static SmoothGlowstoneBlock block;
    public static float hardness = 0.3F;

    @Override
    public void init()
    {
        block = new SmoothGlowstoneBlock();
    }
}
