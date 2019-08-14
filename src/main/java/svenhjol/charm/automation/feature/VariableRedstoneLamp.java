package svenhjol.charm.automation.feature;

import svenhjol.charm.automation.block.VariableRedstoneLampBlock;
import svenhjol.meson.Feature;

public class VariableRedstoneLamp extends Feature
{
    public static VariableRedstoneLampBlock block;

    @Override
    public void init()
    {
        super.init();
        block = new VariableRedstoneLampBlock();
    }
}
