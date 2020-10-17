package svenhjol.charm.module;

import net.minecraft.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.ItemAccessor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Allows potions to stack.")
public class StackablePotions extends CharmModule {
    @Config(name = "Stack size", description = "Maximum potion stack size.")
    public static int stackSize = 16;

    @Override
    public void init() {
        ((ItemAccessor) Items.POTION).setMaxCount(stackSize);
        ((ItemAccessor) Items.SPLASH_POTION).setMaxCount(stackSize);
        ((ItemAccessor) Items.LINGERING_POTION).setMaxCount(stackSize);
    }
}
