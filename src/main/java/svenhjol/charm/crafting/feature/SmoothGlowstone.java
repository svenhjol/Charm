package svenhjol.charm.crafting.feature;

import svenhjol.charm.crafting.block.SmoothGlowstoneBlock;
import svenhjol.meson.Feature;

public class SmoothGlowstone extends Feature
{
    public static SmoothGlowstoneBlock block;
    public static float hardness;

    @Override
    public void configure()
    {
        super.configure();

        hardness = 0.3F;
    }

    @Override
    public void init()
    {
        super.init();

        block = new SmoothGlowstoneBlock();
    }
}
