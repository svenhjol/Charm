package svenhjol.charm.crafting.feature;

import svenhjol.charm.crafting.block.RottenFleshBlockBlock;
import svenhjol.meson.Feature;

public class RottenFleshBlock extends Feature
{
    public static RottenFleshBlockBlock block;
    public static float hardness;
    public static float resistance;

    @Override
    public void configure()
    {
        super.configure();
        hardness = 1.0F;
        resistance = 2.0F;
    }

    @Override
    public void init()
    {
        super.init();
        block = new RottenFleshBlockBlock();
    }
}
