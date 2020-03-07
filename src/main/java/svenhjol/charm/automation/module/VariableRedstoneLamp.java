package svenhjol.charm.automation.module;

import svenhjol.charm.Charm;
import svenhjol.charm.automation.block.VariableRedstoneLampBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.AUTOMATION,
    description = "A block that emits light according to the strength of the input redstone signal.")
public class VariableRedstoneLamp extends MesonModule {
    public static VariableRedstoneLampBlock block;

    @Override
    public void init() {
        block = new VariableRedstoneLampBlock(this);
    }
}
