package svenhjol.charm.module.stackable_potions;

import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Allows potions to stack.")
public class StackablePotions extends CharmModule {
    @Config(name = "Stack size", description = "Maximum potion stack size.")
    public static int stackSize = 16;

    @Override
    public void runWhenEnabled() {
        Items.POTION.maxStackSize = stackSize;
        Items.SPLASH_POTION.maxStackSize = stackSize;
        Items.LINGERING_POTION.maxStackSize = stackSize;
    }
}
