package svenhjol.charm.module;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.ItemAccessor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Allows enchanted books to stack.")
public class StackableEnchantedBooks extends CharmModule {
    @Config(name = "Stack size", description = "Maximum enchanted book stack size.")
    public static int stackSize = 16;

    @Override
    public void init() {
        ((ItemAccessor)Items.ENCHANTED_BOOK).setMaxCount(stackSize);
    }

    public static ItemStack getReducedStack(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
            stack.decrement(1);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
