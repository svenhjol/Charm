package svenhjol.charm.automation.module;

import svenhjol.charm.automation.block.VariableRedstoneLampBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(category = CharmCategories.AUTOMATION)
public class VariableRedstoneLamp extends MesonModule
{
    public static VariableRedstoneLampBlock block;

    @Override
    public void init()
    {
        block = new VariableRedstoneLampBlock();
    }
}
