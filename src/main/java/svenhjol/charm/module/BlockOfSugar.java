package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.block.SugarBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "A storage block for sugar. It obeys gravity and dissolves in water.")
public class BlockOfSugar extends CharmModule {
    public static SugarBlock SUGAR_BLOCK;

    @Override
    public void register() {
        SUGAR_BLOCK = new SugarBlock(this);
    }
}
