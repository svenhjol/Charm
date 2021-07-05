package svenhjol.charm.mixin.stackable_enchanted_books;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import svenhjol.charm.Charm;
import svenhjol.charm.module.stackable_enchanted_books.StackableEnchantedBooks;

@Mixin(AnvilMenu.class)
public class FixAnvilBookStackMixin {
    /**
     * When adding a stack of enchanted books to an item on the anvil,
     * the entire stack is lost.  The vanilla logic does not take into
     * account an enchanted book stack.  This mixin calls getReducedStack
     * to properly decrement the stack, then sets the updated stack
     * back to the slot.
     */
    @Redirect(
        method = "onTake",
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
            )
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V",
            opcode = Opcodes.INVOKEINTERFACE,
            ordinal = 2
        )
    )
    private void anvilUpdateHook(Container inv, int index, ItemStack stack) {
        if (Charm.LOADER.isEnabled(StackableEnchantedBooks.class))
            stack = StackableEnchantedBooks.getReducedStack(inv.getItem(index));

        inv.setItem(index, stack);
    }
}
