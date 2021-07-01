package svenhjol.charm.module.stackable_potions;

import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.ItemAccessor;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Allows potions to stack.")
public class StackablePotions extends svenhjol.charm.loader.CommonModule {
    @Config(name = "Stack size", description = "Maximum potion stack size.")
    public static int stackSize = 16;

    @Override
    public void run() {
        ((ItemAccessor) Items.POTION).setMaxStackSize(stackSize);
        ((ItemAccessor) Items.SPLASH_POTION).setMaxStackSize(stackSize);
        ((ItemAccessor) Items.LINGERING_POTION).setMaxStackSize(stackSize);
    }
}
