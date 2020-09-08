package svenhjol.charm.module;

import svenhjol.charm.block.SugarBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "A storage block for sugar. It obeys gravity and dissolves in water.")
public class BlockOfSugar extends MesonModule {
    public static SugarBlock SUGAR_BLOCK;

    @Override
    public void register() {
        SUGAR_BLOCK = new SugarBlock(this);
    }
}
