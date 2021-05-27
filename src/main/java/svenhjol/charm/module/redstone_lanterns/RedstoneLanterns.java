package svenhjol.charm.module.redstone_lanterns;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, client = RedstoneLanternsClient.class, description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLanterns extends CharmModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Override
    public void register() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
    }
}
