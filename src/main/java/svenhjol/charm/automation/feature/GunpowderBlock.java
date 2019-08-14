package svenhjol.charm.automation.feature;

import svenhjol.charm.automation.block.GunpowderBlockBlock;
import svenhjol.meson.Feature;

public class GunpowderBlock extends Feature
{
    public static GunpowderBlockBlock block;

    @Override
    public void init()
    {
        super.init();
        block = new GunpowderBlockBlock();
    }
}
