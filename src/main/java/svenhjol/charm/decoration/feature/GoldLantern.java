package svenhjol.charm.decoration.feature;

import svenhjol.charm.decoration.block.GoldLanternBlock;
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
