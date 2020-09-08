package svenhjol.charm.module;

import net.minecraft.item.Items;
import svenhjol.meson.mixin.accessor.ItemAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Allows potions to stack.")
public class StackablePotions extends MesonModule {
    @Config(name = "Stack size", description = "Maximum potion stack size.")
    public static int stackSize = 16;

    @Override
    public void init() {
        ((ItemAccessor) Items.POTION).setMaxCount(stackSize);
        ((ItemAccessor) Items.SPLASH_POTION).setMaxCount(stackSize);
        ((ItemAccessor) Items.LINGERING_POTION).setMaxCount(stackSize);
    }
}
