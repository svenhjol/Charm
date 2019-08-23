package svenhjol.charm.automation.module;

import svenhjol.charm.Charm;
import svenhjol.charm.automation.block.GunpowderBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.AUTOMATION)
public class BlockOfGunpowder extends MesonModule
{
    public static GunpowderBlock block;

    @Override
    public void init()
    {
        block = new GunpowderBlock(this);
    }
}
