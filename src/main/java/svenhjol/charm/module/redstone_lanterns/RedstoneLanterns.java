package svenhjol.charm.module.redstone_lanterns;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLanterns extends svenhjol.charm.loader.CommonModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Override
    public void register() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
    }
}
