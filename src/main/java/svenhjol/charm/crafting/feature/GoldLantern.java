package svenhjol.charm.crafting.feature;

import svenhjol.charm.crafting.block.GoldLanternBlock;
import svenhjol.meson.Feature;

public class GoldLantern extends Feature
{
    public static GoldLanternBlock block;

    @Override
    public void init()
    {
        super.init();
        block = new GoldLanternBlock();
    }
}
