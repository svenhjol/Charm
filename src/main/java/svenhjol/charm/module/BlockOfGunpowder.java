package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.block.GunpowderBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "A storage block for gunpowder. It obeys gravity and dissolves in lava.")
public class BlockOfGunpowder extends CharmModule {
    public static GunpowderBlock GUNPOWDER_BLOCK;

    @Override
    public void register() {
        GUNPOWDER_BLOCK = new GunpowderBlock(this);
    }
}
