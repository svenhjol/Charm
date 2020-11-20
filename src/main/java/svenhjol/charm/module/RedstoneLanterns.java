package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.RedstoneLanternBlock;
import svenhjol.charm.client.RedstoneLanternsClient;

@Module(mod = Charm.MOD_ID, client = RedstoneLanternsClient.class, description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLanterns extends CharmModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Override
    public void register() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
    }
}
