package svenhjol.charm.automation.feature;

import svenhjol.charm.automation.block.RedstoneSandBlock;
import svenhjol.meson.Feature;

public class RedstoneSand extends Feature
{
    public static RedstoneSandBlock block;

    @Override
    public void init()
    {
        super.init();
        block = new RedstoneSandBlock();
    }
}
