package svenhjol.charm.module.stackable_enchanted_books;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.mixin.accessor.ItemAccessor;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Allows enchanted books to stack.")
public class StackableEnchantedBooks extends CharmCommonModule {
    @Config(name = "Stack size", description = "Maximum enchanted book stack size.")
    public static int stackSize = 16;

    @Override
    public void run() {
        ((ItemAccessor)Items.ENCHANTED_BOOK).setMaxStackSize(stackSize);
    }

    public static ItemStack getReducedStack(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
            stack.shrink(1);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
